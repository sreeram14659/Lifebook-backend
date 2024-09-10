package com.lifebook.Lifebook.model.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetMemoriesRequest extends GenericRequest {

    @Override
    public String toString() {
        return "GetMemoriesRequest{" +
            "entityType='" + getEntityType() + '\'' +
            ", action='" + getAction() + '\'' +
            '}';
    }
}