package com.lifebook.Lifebook.model.requests;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetSongByIdRequest extends GenericRequest {
    private String songId;

    @Override
    public String toString() {
        return "GetSongByIdRequest{" +
            "entityType='" + getEntityType() + '\'' +
            ", action='" + getAction() + '\'' +
            ", songId='" + songId + '\'' +
            '}';
    }
}
