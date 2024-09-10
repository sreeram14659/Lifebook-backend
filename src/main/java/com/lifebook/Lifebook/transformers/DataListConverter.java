package com.lifebook.Lifebook.transformers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.lifebook.Lifebook.model.Data;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.List;

public class DataListConverter implements AttributeConverter<List<Data>> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final CollectionType LIST_TYPE = OBJECT_MAPPER.getTypeFactory()
        .constructCollectionType(List.class, Data.class);

    @Override
    public AttributeValue transformFrom(List<Data> input) {
        try {
            // Convert List<Data> to JSON string
            String jsonString = OBJECT_MAPPER.writeValueAsString(input);
            return AttributeValue.builder().s(jsonString).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert List<Data> to JSON", e);
        }
    }

    @Override
    public List<Data> transformTo(AttributeValue input) {
        if (input.s() == null) {
            return List.of();
        }
        try {
            // Convert JSON string to List<Data>
            return OBJECT_MAPPER.readValue(input.s(), LIST_TYPE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert JSON to List<Data>", e);
        }
    }

    @Override
    public EnhancedType<List<Data>> type() {
        return EnhancedType.listOf(Data.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}

