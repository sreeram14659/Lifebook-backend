package com.lifebook.Lifebook.model.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetMemoryByIdRequest extends GenericRequest {
    private String memoryId;

    @Override
    public String toString() {
        return "GetMemoryByIdRequest{" +
            "entityType='" + getEntityType() + '\'' +
            ", action='" + getAction() + '\'' +
            ", memoryId='" + memoryId + '\'' +
            '}';
    }
}