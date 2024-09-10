package com.lifebook.Lifebook.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class Configuration {

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
//        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        return mapper;
    }
}
