package com.lifebook.Lifebook.fetcher;

import com.lifebook.Lifebook.dynamodb.DynamoDBStore;
import com.lifebook.Lifebook.mappers.UnifiedEntityMapper;
import com.lifebook.Lifebook.model.EntityType;
import com.lifebook.Lifebook.model.UnifiedEntity;
import com.lifebook.Lifebook.model.requests.GetMemoriesRequest;
import com.lifebook.Lifebook.model.requests.GetMemoryByIdRequest;
import com.lifebook.Lifebook.model.responses.GetMemoriesResponse;
import com.lifebook.Lifebook.model.responses.GetMemoryByIdResponse;
import com.lifebook.Lifebook.model.types.Memory;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MemoriesDataManager {
    private static final String ENTITY_PRIORITY_INDEX = "entityType-priority-index";
    private final DynamoDBStore<UnifiedEntity> dynamoDBStore;

    public GetMemoriesResponse fetchMemories(final GetMemoriesRequest getMemoriesRequest) {
        QueryEnhancedRequest queryRequest = dynamoDBStore.buildQueryEnhancedRequest("Memory", null);
        List<UnifiedEntity> rawResults = dynamoDBStore.queryIndex(ENTITY_PRIORITY_INDEX, queryRequest);

        System.out.println("The raw results are: " + rawResults);

        List<Memory> memories = rawResults.stream()
            .filter(entity -> entity.getEntityType() == EntityType.Memory)
            .map(UnifiedEntityMapper.INSTANCE::toMemory)
            .collect(Collectors.toList());

        return GetMemoriesResponse.builder()
            .memories(memories)
            .build();
    }

    public GetMemoryByIdResponse fetchMemoryById(final GetMemoryByIdRequest getMemoryByIdRequest) {
        UnifiedEntity entity = dynamoDBStore.get(getMemoryByIdRequest.getMemoryId(), EntityType.Memory.name());
        System.out.println("Entity is: " + entity);
        if (entity != null && entity.getEntityType() == EntityType.Memory) {
            return GetMemoryByIdResponse.builder()
                .memory(UnifiedEntityMapper.INSTANCE.toMemory(entity))
                .build();
        } else {
            throw new RuntimeException("Memory not found");
        }
    }

    public void putMemory(final Memory memory) {
        System.out.println("Attempting to put memory: " + memory);
        UnifiedEntity unifiedEntity = UnifiedEntityMapper.INSTANCE.toUnifiedEntity(memory);
        if (unifiedEntity.getEntityType() == EntityType.Memory) {
            dynamoDBStore.put(unifiedEntity);
        } else {
            throw new IllegalArgumentException("Entity must be of type 'Memory'");
        }
    }
}
