package com.lifebook.Lifebook.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lifebook.Lifebook.configuration.Configuration;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class GenerateS3SignedUrlHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final String BUCKET_NAME = "lifebook-images";

    private final ObjectMapper objectMapper;

    public GenerateS3SignedUrlHandler() {
        this.objectMapper = Configuration.objectMapper();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        try {
            System.out.println("I am being invoked with payload: " + objectMapper.writeValueAsString(input));
            final Map<String, String> requestBody = objectMapper.readValue(input.getBody(), Map.class);
            final String fileName = requestBody.get("fileName");
            final String fileType = requestBody.get("fileType");  // This is not used in generating the presigned URL
            final String id = UUID.randomUUID().toString();

            if (fileName == null || fileType == null) {
                return new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody("fileName and fileType are required.");
            }

            final String objectKey = "memories/" + id + "/" + fileName;
            final String preSignedUrl = createPresignedPutUrl(BUCKET_NAME, objectKey);

            Map<String, String> responseBody = Map.of(
                "url", preSignedUrl,
                "filePath", objectKey
            );

            return new APIGatewayProxyResponseEvent()
                .withStatusCode(200)
                .withBody(objectMapper.writeValueAsString(responseBody))
                .withHeaders(Map.of("Content-Type", "application/json"));

        } catch (SdkException e) {
            String errorMessage = "SDK Error: " + e.getMessage();
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody(errorMessage)
                .withHeaders(Map.of("Content-Type", "text/plain"));
        } catch (Exception e) {
            return new APIGatewayProxyResponseEvent()
                .withStatusCode(500)
                .withBody("Internal Server Error: " + e.getMessage())
                .withHeaders(Map.of("Content-Type", "text/plain"));
        }
    }

    /* Create a pre-signed URL to download an object in a subsequent GET request. */
    public String createPresignedPutUrl(String bucketName, String keyName) {
        try (S3Presigner presigner = S3Presigner.create()) {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))  // The URL expires in 10 minutes.
                .putObjectRequest(objectRequest)
                .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String myURL = presignedRequest.url().toString();
            System.out.println("Pre-signed URL to upload a file to: " +  myURL);
            System.out.println("HTTP method: " +  presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }

}
