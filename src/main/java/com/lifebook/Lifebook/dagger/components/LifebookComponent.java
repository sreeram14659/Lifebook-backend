package com.lifebook.Lifebook.dagger.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifebook.Lifebook.dagger.modules.Configuration;
import com.lifebook.Lifebook.dagger.modules.DynamoDBConfig;
import com.lifebook.Lifebook.dynamodb.DynamoDBStore;
import com.lifebook.Lifebook.fetcher.MemoriesDataManager;
import com.lifebook.Lifebook.fetcher.SongsDataManager;
import com.lifebook.Lifebook.model.UnifiedEntity;
import dagger.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import javax.inject.Singleton;

@Singleton
@Component(modules = {DynamoDBConfig.class, Configuration.class})
public interface LifebookComponent {
    ObjectMapper objectMapper();

    DynamoDbEnhancedClient getEnhancedClient();

    DynamoDbTable<UnifiedEntity> getLifeEntitiesTable();

    DynamoDBStore getDynamoDBStore();

    MemoriesDataManager memoriesDataManager();

    SongsDataManager songsDataManager();
}
