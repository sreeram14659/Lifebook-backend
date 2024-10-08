//package com.lifebook.Lifebook.handlers;
//
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
//import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lifebook.Lifebook.dagger.modules.Configuration;
//import com.lifebook.Lifebook.dynamodb.DynamoDBStore;
//import com.lifebook.Lifebook.fetcher.SongsDataManager;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class SongsHandlerTest {
//    private final ObjectMapper objectMapper = Configuration.objectMapper();
//    @Mock
//    private DynamoDBStore dynamoDBStore;
//    @Mock
//    private DynamoDBItemConverter dynamoDBItemConverter;
//
//    private final SongHandler songsHandler = new SongHandler(objectMapper, new SongsDataManager(objectMapper, dynamoDBStore, dynamoDBItemConverter));
//
//    @Test
//    void testGetSongByIdRequest() {
//        Map<String, String> pathParameters = new HashMap<>();
//        pathParameters.put("id", "123");
//
//        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
//            .withPath("/songs/{id}")
//            .withPathParameters(pathParameters);
//        request.setHttpMethod("GET");
//
//        APIGatewayProxyResponseEvent response = songsHandler.handleRequest(request, null);
//        System.out.println("Song: " + response);
//        assertEquals(200, response.getStatusCode().intValue());
//        // Add more assertions to verify the response body or other properties
//    }
//
//    @Test
//    void testGetSongsRequest() {
//        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
//            .withPath("/songs");
//        request.setHttpMethod("GET");
//        APIGatewayProxyResponseEvent response = songsHandler.handleRequest(request, null);
//        System.out.println("Songs: " + response);
//        assertEquals(200, response.getStatusCode().intValue());
//        // Add more assertions to verify the response body or other properties
//    }
//}
