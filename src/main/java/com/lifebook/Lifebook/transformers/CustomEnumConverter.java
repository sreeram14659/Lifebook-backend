package com.lifebook.Lifebook.transformers;

import com.lifebook.Lifebook.model.EntityType;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class CustomEnumConverter implements AttributeConverter<EntityType> {

    @Override
    public AttributeValue transformFrom(EntityType input) {
        return AttributeValue.builder().s(input.name()).build();
    }

    @Override
    public EntityType transformTo(AttributeValue input) {
        return EntityType.valueOf(input.s());
    }

    @Override
    public EnhancedType<EntityType> type() {
        return EnhancedType.of(EntityType.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}

