package com.lifebook.Lifebook.fetcher;

import com.lifebook.Lifebook.dynamodb.DynamoDBStore;
import com.lifebook.Lifebook.mappers.UnifiedEntityMapper;
import com.lifebook.Lifebook.model.EntityType;
import com.lifebook.Lifebook.model.UnifiedEntity;
import com.lifebook.Lifebook.model.requests.GetSongByIdRequest;
import com.lifebook.Lifebook.model.requests.GetSongsRequest;
import com.lifebook.Lifebook.model.responses.GetSongByIdResponse;
import com.lifebook.Lifebook.model.responses.GetSongsResponse;
import com.lifebook.Lifebook.model.types.Song;
import lombok.AllArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SongsDataManager {
    private static final String ENTITY_PRIORITY_INDEX = "entityType-priority-index";
    private final DynamoDBStore<UnifiedEntity> dynamoDBStore;

    public GetSongsResponse fetchSongs(final GetSongsRequest getSongsRequest) {
        QueryEnhancedRequest queryRequest = dynamoDBStore.buildQueryEnhancedRequest("Song", null);
        List<UnifiedEntity> rawResults = dynamoDBStore.queryIndex(ENTITY_PRIORITY_INDEX, queryRequest);

        System.out.println("The raw results are: " + rawResults);

        List<Song> songs = rawResults.stream()
            .filter(entity -> entity.getEntityType() == EntityType.Song)
            .map(UnifiedEntityMapper.INSTANCE::toSong)
            .collect(Collectors.toList());

        return GetSongsResponse.builder()
            .songs(songs)
            .build();
    }

    public GetSongByIdResponse fetchSongById(final GetSongByIdRequest getSongByIdRequest) {
        UnifiedEntity entity = dynamoDBStore.get(getSongByIdRequest.getSongId(), EntityType.Song.name());
        System.out.println("Entity is: " + entity);
        if (entity != null && entity.getEntityType() == EntityType.Song) {
            return GetSongByIdResponse.builder()
                .song(UnifiedEntityMapper.INSTANCE.toSong(entity))
                .build();
        } else {
            throw new RuntimeException("Song not found");
        }
    }

    public void putSong(final Song song) {
        System.out.println("Attempting to put Song: " + song);
        UnifiedEntity unifiedEntity = UnifiedEntityMapper.INSTANCE.toUnifiedEntity(song);
        if (unifiedEntity.getEntityType() == EntityType.Song) {
            dynamoDBStore.put(unifiedEntity);
        } else {
            throw new IllegalArgumentException("Entity must be of type 'Song'");
        }
    }

    public UnifiedEntity deleteSong(final String songId) {
        UnifiedEntity entity = dynamoDBStore.get(songId, EntityType.Song.name());
        if (entity != null && entity.getEntityType() == EntityType.Song) {
            return dynamoDBStore.delete(entity);
        } else {
            throw new RuntimeException("Song not found");
        }
    }
}
