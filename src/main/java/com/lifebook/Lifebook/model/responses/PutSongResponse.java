package com.lifebook.Lifebook.model.responses;

import com.lifebook.Lifebook.model.EntityType;
import com.lifebook.Lifebook.model.PrivacyLevel;
import com.lifebook.Lifebook.model.types.Song;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class PutSongResponse {
    private String id;
    private String name;
    private Instant creationDate;
    private Instant modifiedDate;
    private PrivacyLevel privacy;
    private boolean active;
    private List<com.lifebook.Lifebook.model.Data> dataList;
    private List<String> tags;
    private List<String> sharedWith;

    private EntityType entityType;
    private int priority;

    private String songUrl;
    private String albumArtUrl;

    public static PutSongResponse fromSongEntity(Song song) {
        return PutSongResponse.builder()
            .id(song.getId())
            .name(song.getName())
            .creationDate(song.getCreationDate())
            .modifiedDate(song.getModifiedDate())
            .privacy(song.getPrivacy())
            .active(song.isActive())
            .dataList(song.getDataList())
            .tags(song.getTags())
            .sharedWith(song.getSharedWith())
            .entityType(song.getEntityType())
            .priority(song.getPriority())
            .songUrl(song.getSongUrl())
            .albumArtUrl(song.getAlbumArtUrl())
            .build();
    }
}
