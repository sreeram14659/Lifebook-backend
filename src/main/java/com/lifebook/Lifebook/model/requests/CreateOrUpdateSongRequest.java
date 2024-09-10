package com.lifebook.Lifebook.model.requests;

import com.lifebook.Lifebook.model.Data;
import com.lifebook.Lifebook.model.PrivacyLevel;
import com.lifebook.Lifebook.model.types.Song;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@SuperBuilder
@Jacksonized
public class CreateOrUpdateSongRequest extends GenericRequest {
    private String id;
    private String name;
    private Instant creationDate;
    private PrivacyLevel privacy;
    private boolean active;
    private List<Data> dataList;
    private List<String> tags;
    private List<String> sharedWith;

    private int priority;

    private String songUrl;
    private String albumArtUrl;

    public static Song toSongEntity(CreateOrUpdateSongRequest request) {
        return Song.builder()
            .songUrl(request.getSongUrl())
            .albumArtUrl(request.getAlbumArtUrl())
            .id(Optional.ofNullable(request.getId()).orElse(UUID.randomUUID().toString()))
            .name(request.getName())
            .creationDate(request.getCreationDate())
            .modifiedDate(Instant.now())
            .privacy(request.getPrivacy())
            .active(request.isActive())
            .dataList(request.getDataList())
            .tags(request.getTags())
            .sharedWith(request.getSharedWith())
            .entityType(request.getEntityType())
            .priority(request.getPriority())
            .build();
    }
}
