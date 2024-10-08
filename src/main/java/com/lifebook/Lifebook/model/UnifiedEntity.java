package com.lifebook.Lifebook.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lifebook.Lifebook.model.types.Memory;
import com.lifebook.Lifebook.model.types.Song;
import com.lifebook.Lifebook.transformers.CustomEnumConverter;
import com.lifebook.Lifebook.transformers.DataListConverter;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAutoGeneratedTimestampAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.UpdateBehavior;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbUpdateBehavior;

import java.time.Instant;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "entityType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Memory.class, name = "Memory"),
    @JsonSubTypes.Type(value = Song.class, name = "Song")
})
@Data
@NoArgsConstructor
@DynamoDbBean
@SuperBuilder
public class UnifiedEntity {
    @Getter(onMethod_ = @DynamoDbPartitionKey)
    private String id;
    @Getter(onMethod = @__({ @DynamoDbSortKey, @DynamoDbSecondaryPartitionKey(indexNames = {"entityType-priority-index"}),
        @DynamoDbAttribute(value = "entityType"), @DynamoDbConvertedBy(CustomEnumConverter.class) }))
    private EntityType entityType;
    @Getter(onMethod = @__({ @DynamoDbSecondarySortKey(indexNames = "entityType-priority-Index"), @DynamoDbAttribute(value = "priority") }))
    private int priority;
    private String name;
    @Getter(onMethod_ = {@DynamoDbAutoGeneratedTimestampAttribute, @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_IF_NOT_EXISTS)})
    private Instant creationDate;
    @Getter(onMethod_ = {@DynamoDbAutoGeneratedTimestampAttribute, @DynamoDbUpdateBehavior(UpdateBehavior.WRITE_ALWAYS)})
    private Instant modifiedDate;
    private PrivacyLevel privacy;
    private boolean active;
    @Getter(onMethod = @__({ @DynamoDbAttribute(value = "dataList"), @DynamoDbConvertedBy(DataListConverter.class) }))
    private List<com.lifebook.Lifebook.model.Data> dataList;
    private List<String> tags;
    private List<String> sharedWith;
}

