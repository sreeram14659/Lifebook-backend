package com.lifebook.Lifebook.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws")
public class AwsConfig {

    private String accessKeyId;

    private String secretKey;

    // Getters and setters
}
