package com.lifebook.Lifebook.transformers;

import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObjectMapAttributeConverter implements AttributeConverter<Map<String, Object>> {

    @Override
    public AttributeValue transformFrom(Map<String, Object> input) {
        return transformToAttributeValue(input);
    }

    @Override
    public Map<String, Object> transformTo(AttributeValue input) {
        if (input.hasM()) {
            return transformToMap(input);
        }
        return new HashMap<>();
    }

    @Override
    public EnhancedType<Map<String, Object>> type() {
        return EnhancedType.mapOf(String.class, Object.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.M;
    }

    /**
     * Transforms the given object to an {@link AttributeValue} for DynamoDB.
     */
    private AttributeValue transformToAttributeValue(Object input) {

        if (input == null) {
            return AttributeValue.fromNul(true);
        }

        if (input instanceof Map) {
            return AttributeValue.builder()
                .m(((Map<String, ?>) input)
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> transformToAttributeValue(e.getValue()))))
                .build();
        }

        if (input instanceof List) {
            return AttributeValue.builder().l(
                    ((List<?>) input).stream()
                        .map(this::transformToAttributeValue)
                        .collect(Collectors.toList()))
                .build();
        }
        if (input instanceof Boolean) {
            return AttributeValue.builder().bool((Boolean) input).build();
        }
        if (input instanceof String) {
            return AttributeValue.builder().s((String) input).build();
        }
        if (input instanceof Number) {
            return AttributeValue.builder().n(String.valueOf(input)).build();
        }

        throw new IllegalArgumentException("Cannot deserialize the given AttributeValue" + input);
    }

    private Object transformToObject(AttributeValue input) {
        // map
        if (input.hasM()) {
            return transformToMap(input);
        }

        // list
        if (input.hasL()) {
            return input.l().stream()
                .map(this::transformToObject)
                .collect(Collectors.toList());
        }
        // string set
        if (input.hasSs()) {
            return input.ss();
        }
        // convert number sets to list of Double/Longs
        if (input.hasNs()) {
            return input.ns().stream().map(this::extractNumber).collect(Collectors.toList());
        }
        // string
        if (input.s() != null) {
            return input.s();
        }
        // convert number to Double if decimal, otherwise to Long
        if (input.n() != null) {
            return extractNumber(input.n());
        }
        // boolean
        if (input.bool() != null) {
            return input.bool();
        }
        if (input.nul() != null && input.nul()) {
            return null;
        }

        throw new IllegalArgumentException("cannot deserialize the given AttributeValue" + input);
    }

    private Object extractNumber(String input) {
        Optional<Double> decimal = Optional
            .of(Double.parseDouble(input))
            .filter(d -> d % 1 != 0);

        if (decimal.isPresent()) return decimal.get();

        return Long.parseLong(input);
    }

    private Map<String, Object> transformToMap(AttributeValue input) {

        Map<String, Object> resultMap = new LinkedHashMap<>();

        input.m().forEach((key, value) -> resultMap.put(key, transformToObject(value)));

        return resultMap;

    }
}