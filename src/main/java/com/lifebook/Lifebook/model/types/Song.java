package com.lifebook.Lifebook.model.types;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.UnifiedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonTypeName("Song")
public class Song extends UnifiedEntity {
    private String songUrl;
    private String albumArtUrl;

    @DynamoDbAttribute("songUrl")
    public String getSongUrl() {
        return songUrl;
    }
    @DynamoDbAttribute("albumArtUrl")
    public String getAlbumArtUrl() {
        return albumArtUrl;
    }
}
