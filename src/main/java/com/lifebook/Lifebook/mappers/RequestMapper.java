package com.lifebook.Lifebook.mappers;

import com.lifebook.Lifebook.model.requests.CreateOrUpdateMemoryRequest;
import com.lifebook.Lifebook.model.requests.CreateOrUpdateSongRequest;
import com.lifebook.Lifebook.model.types.Memory;
import com.lifebook.Lifebook.model.types.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "modifiedDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "id", expression = "java(request.getId() != null ? request.getId() : java.util.UUID.randomUUID().toString())")
    @Mapping(target = "priority", source = "request", qualifiedByName = "mapPriorityForMemory")
    Memory toMemory(CreateOrUpdateMemoryRequest request);

    @Mapping(target = "modifiedDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "id", expression = "java(request.getId() != null ? request.getId() : java.util.UUID.randomUUID().toString())")
    @Mapping(target = "priority", source = "request", qualifiedByName = "mapPriorityForSong")
    @Mapping(target = "songUrl", source = "songUrl")
    @Mapping(target = "albumArtUrl", source = "albumArtUrl")
    @Mapping(target = "viewed", source = "viewed")
    Song toSong(CreateOrUpdateSongRequest request);

    // Mapping priorities for Memory
    @Named("mapPriorityForMemory")
    default int mapPriorityForMemory(CreateOrUpdateMemoryRequest request) {
        return request.getPriority() > 0 ? request.getPriority() : generateRandomPriority();
    }

    // Mapping priorities for Song
    @Named("mapPriorityForSong")
    default int mapPriorityForSong(CreateOrUpdateSongRequest request) {
        return request.getPriority() > 0 ? request.getPriority() : generateRandomPriority();
    }


    default int generateRandomPriority() {
        return (int) (Math.random() * (1000 - 20 + 1)) + 20;
    }
}
