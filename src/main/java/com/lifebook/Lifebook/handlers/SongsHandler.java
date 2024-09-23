package com.lifebook.Lifebook.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifebook.Lifebook.configuration.Configuration;
import com.lifebook.Lifebook.configuration.DynamoDBConfig;
import com.lifebook.Lifebook.dynamodb.DynamoDBStore;
import com.lifebook.Lifebook.fetcher.SongsDataManager;
import com.lifebook.Lifebook.mappers.RequestMapper;
import com.lifebook.Lifebook.model.UnifiedEntity;
import com.lifebook.Lifebook.model.requests.CreateOrUpdateSongRequest;
import com.lifebook.Lifebook.model.requests.GetSongByIdRequest;
import com.lifebook.Lifebook.model.requests.GetSongsRequest;
import com.lifebook.Lifebook.model.responses.GetSongByIdResponse;
import com.lifebook.Lifebook.model.responses.GetSongsResponse;
import com.lifebook.Lifebook.model.types.Song;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class SongsHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String LIFE_ENTITIES_TABLE = "LifeEntity";
    private static final String GET_SONGS_BY_ID_PATH_REGEX = "^/default/song/[^/]+$";
    private static final String GET_SONGS_PATH = "/default/songs";
    private static final String POST_SONG_PATH = "/default/song";

    private final ObjectMapper objectMapper;
    private final SongsDataManager songsDataManager;

    public SongsHandler() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBConfig.createEnhancedClient();
        DynamoDbTable<UnifiedEntity> lifeEntityTable = enhancedClient
            .table(LIFE_ENTITIES_TABLE, TableSchema.fromBean(UnifiedEntity.class));
        this.objectMapper = Configuration.objectMapper();
        this.songsDataManager = new SongsDataManager(new DynamoDBStore<UnifiedEntity>(lifeEntityTable, enhancedClient));
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        System.out.println("I am being invoked: " + input);
        String httpMethod = input.getHttpMethod();
        String path = input.getPath();
        APIGatewayProxyResponseEvent response;

        try {
            if ("GET".equals(httpMethod)) {
                if (path.matches(GET_SONGS_BY_ID_PATH_REGEX)) {
                    response = handleGetSongByIdRequest(input);
                } else if (GET_SONGS_PATH.equals(path)) {
                    response = handleGetSongsRequest(input);
                } else {
                    response = new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("The path provided for GET is invalid.");
                }
            } else if ("POST".equals(httpMethod) && POST_SONG_PATH.equals(path)) {
                response = handlePutSongRequest(input);
            } else if ("DELETE".equals(httpMethod) && path.matches(GET_SONGS_BY_ID_PATH_REGEX)) {
                response = handleDeleteSongRequest(input);
            } else {
                response = new APIGatewayProxyResponseEvent().withStatusCode(405).withBody("HTTP Method Not Allowed");
            }
        } catch (Exception e) {
            response = new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Internal Server Error: " + e.getMessage());
        }
        return response;
    }

    private APIGatewayProxyResponseEvent handlePutSongRequest(final APIGatewayProxyRequestEvent input) {
        try {
            final CreateOrUpdateSongRequest request = objectMapper.readValue(input.getBody(), CreateOrUpdateSongRequest.class);
            final Song song = RequestMapper.INSTANCE.toSong(request);
            songsDataManager.putSong(song);
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(song)).withHeaders(Map.of("Content-Type", "application/json"));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error processing request: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent handleGetSongsRequest(final APIGatewayProxyRequestEvent input) throws JsonProcessingException {
        GetSongsResponse getSongsResponse = songsDataManager.fetchSongs(GetSongsRequest.builder().build());
        return new APIGatewayProxyResponseEvent()
            .withStatusCode(200)
            .withBody(objectMapper.writeValueAsString(getSongsResponse));
    }

    private APIGatewayProxyResponseEvent handleGetSongByIdRequest(final APIGatewayProxyRequestEvent input) throws JsonProcessingException {
        try {
            Map<String, String> pathParameters = input.getPathParameters();
            String songId = pathParameters.get("id");
            GetSongByIdResponse getsongByIdResponse = Optional.ofNullable(songId).map(id -> {
                GetSongByIdRequest request = GetSongByIdRequest.builder().songId(id).build();
                return songsDataManager.fetchSongById(request);
            }).orElseThrow(() -> new IllegalStateException("song ID required"));
            return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(getsongByIdResponse));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error processing the deletion request: " + e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent handleDeleteSongRequest(final APIGatewayProxyRequestEvent input) {
        try {
            Map<String, String> pathParameters = input.getPathParameters();
            String songId = pathParameters.get("id");
            UnifiedEntity deletedEntity = Optional.ofNullable(songId).map(id -> songsDataManager.deleteSong(songId))
                .orElseThrow(() -> new IllegalStateException("Song ID required for deletion"));
            if (deletedEntity != null) {
                return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(objectMapper.writeValueAsString(deletedEntity));
            } else {
                return new APIGatewayProxyResponseEvent().withStatusCode(404).withBody("Song not found");
            }
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent().withStatusCode(500).withBody("Error processing GetSongById request: " + e.getMessage());
        }
    }
}
