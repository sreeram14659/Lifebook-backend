package com.lifebook.Lifebook.model.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetSongsRequest extends GenericRequest {

    @Override
    public String toString() {
        return "GetSongsRequest{" +
            "entityType='" + getEntityType() + '\'' +
            ", action='" + getAction() + '\'' +
            '}';
    }
}
