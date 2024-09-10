package com.lifebook.Lifebook;

import com.lifebook.Lifebook.configuration.AwsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AwsConfig.class)
public class LifebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(LifebookApplication.class, args);
	}
}
