package com.lifebook.Lifebook.model.requests;

import com.lifebook.Lifebook.model.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@SuperBuilder
@AllArgsConstructor
@Jacksonized
public class GenericRequest {
    private EntityType entityType;
    private String action;

    @Override
    public String toString() {
        return "GenericRequest{" +
            "entityType='" + entityType + '\'' +
            ", action='" + action + '\'' +
            '}';
    }
}