package com.lifebook.Lifebook.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifebook.Lifebook.dagger.components.DaggerLifebookComponent;
import com.lifebook.Lifebook.dagger.components.LifebookComponent;
import com.lifebook.Lifebook.fetcher.MemoriesDataManager;
import com.lifebook.Lifebook.mappers.RequestMapper;
import com.lifebook.Lifebook.model.UnifiedEntity;
import com.lifebook.Lifebook.model.requests.CreateOrUpdateMemoryRequest;
import com.lifebook.Lifebook.model.requests.GetMemoriesRequest;
import com.lifebook.Lifebook.model.requests.GetMemoryByIdRequest;
import com.lifebook.Lifebook.model.responses.GetMemoriesResponse;
import com.lifebook.Lifebook.model.responses.GetMemoryByIdResponse;
import com.lifebook.Lifebook.model.types.Memory;
import lombok.AllArgsConstructor;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class MemoryHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String LIFE_ENTITIES_TABLE = "LifeEntity";
    private static final String GET_MEMORIES_BY_ID_PATH_REGEX = "^/default/memory/[^/]+$";
    private static final String GET_MEMORIES_PATH = "/default/memories";
    private static final String POST_MEMORY_PATH = "/default/memory";

    private final ObjectMapper objectMapper;
    private final MemoriesDataManager memoriesDataManager;

    public MemoryHandler() {
        this(DaggerLifebookComponent.create());
    }

    @Inject
    public MemoryHandler(final LifebookComponent lifebookComponent) {
        this.objectMapper = lifebookComponent.objectMapper();
        this.memoriesDataManager = lifebookComponent.memoriesDataManager();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        System.out.println("I am being invoked: " + input);
        String httpMethod = input.getHttpMethod();
        String path = input.getPath();
        APIGatewayProxyResponseEvent response;

        try {
            if ("GET".equals(httpMethod)) {
                if (path.matches(GET_MEMORIES_BY_ID_PATH_REGEX)) {
                    response = handleGetMemoryByIdRequest(input);
                } else if (GET_MEMORIES_PATH.equals(path)) {
                    response = handleGetMemoriesRequest(input);
                } else {
                    response = new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("The path provided for GET is invalid.");
                }
            } else if ("POST".equals(httpMethod) && POST_MEMORY_PATH.equals(path)) {
                response = handlePutMemoryRequest(input);
            } else if ("DELETE".equals(httpMethod) && path.matches(GET_MEMORIES_BY_ID_PATH_REGEX)) {
                response = handleDeleteMemoryRequest(input);
            } else {
                response = new APIGatewayProxyResponseEvent().withStatusCode(405).withBody("HTTP Method Not Allowed");
            }
        } catch (Exception e) {
            response = new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Internal Server Error: " + e.getMessage());
        }
        return response;
    }

    private APIGatewayProxyResponseEvent handlePutMemoryRequest(final APIGatewayProxyRequestEvent input) {
        try {
            CreateOrUpdateMemoryRequest request = objectMapper.readValue(input.getBody(), CreateOrUpdateMemoryRequest.class);
            Memory memory = RequestMapper.INSTANCE.toMemory(request);
            memoriesDataManager.putMemory(memory);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(memory)).withHeaders(Map.of("Content-Type", "application/json"));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error processing request: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent handleGetMemoriesRequest(final APIGatewayProxyRequestEvent input) throws JsonProcessingException {
        GetMemoriesResponse getMemoriesResponse = memoriesDataManager.fetchMemories(GetMemoriesRequest.builder().build());
        return new APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody(objectMapper.writeValueAsString(getMemoriesResponse));
    }

    private APIGatewayProxyResponseEvent handleGetMemoryByIdRequest(final APIGatewayProxyRequestEvent input) throws JsonProcessingException {
        try {
            Map<String, String> pathParameters = input.getPathParameters();
            String memoryId = pathParameters.get("id");
            GetMemoryByIdResponse getMemoryByIdResponse = Optional.ofNullable(memoryId).map(id -> {
                GetMemoryByIdRequest request = GetMemoryByIdRequest.builder().memoryId(id).build();
                return memoriesDataManager.fetchMemoryById(request);
            }).orElseThrow(() -> new IllegalStateException("Memory ID required"));
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(getMemoryByIdResponse));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error processing the deletion request: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent handleDeleteMemoryRequest(final APIGatewayProxyRequestEvent input) {
        try {
            Map<String, String> pathParameters = input.getPathParameters();
            String memoryId = pathParameters.get("id");
            UnifiedEntity deletedEntity = Optional.ofNullable(memoryId).map(id -> memoriesDataManager.deleteMemory(memoryId)).orElseThrow(() -> new IllegalStateException("Memory ID required for deletion"));
            if (deletedEntity != null) {
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(deletedEntity));
            } else {
                return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Memory not found");
            }
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error processing GetMemoryById request: " + e.getMessage());
        }
    }
}
