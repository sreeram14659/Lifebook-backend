package com.lifebook.Lifebook.dagger.modules;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.lifebook.Lifebook.dynamodb.DynamoDBStore;
import com.lifebook.Lifebook.fetcher.MemoriesDataManager;
import com.lifebook.Lifebook.fetcher.SongsDataManager;
import com.lifebook.Lifebook.model.UnifiedEntity;
import dagger.Module;
import dagger.Provides;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import javax.inject.Singleton;

@Module
public class Configuration {

    @Provides
    @Singleton
    public static ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Register modules for better compatibility
        mapper.registerModule(new JavaTimeModule()); // Support for Java 8 Date/Time API
        mapper.registerModule(new ParameterNamesModule()); // Support for parameter names in constructors
        mapper.registerModule(new Jdk8Module()); // Support for other Java 8 features

        // Serialization settings
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Exclude null values
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Write dates as ISO strings

        // Deserialization settings
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // Ignore unknown properties
        return mapper;
    }

    @Provides
    @Singleton
    public DynamoDBStore dynamoDBStore(DynamoDbEnhancedClient dynamoDbEnhancedClient, DynamoDbTable<UnifiedEntity> table) {
        return new DynamoDBStore(table, dynamoDbEnhancedClient);
    }

    @Provides
    @Singleton
    public MemoriesDataManager memoriesDataManager(DynamoDBStore dynamoDBStore) {
        return new MemoriesDataManager(dynamoDBStore);
    }

    @Provides
    @Singleton
    public SongsDataManager songsDataManager(DynamoDBStore dynamoDBStore) {
        return new SongsDataManager(dynamoDBStore);
    }
}
