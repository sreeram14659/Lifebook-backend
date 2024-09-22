package com.lifebook.Lifebook.mappers;

import com.lifebook.Lifebook.model.requests.CreateOrUpdateMemoryRequest;
import com.lifebook.Lifebook.model.types.Memory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

    @Mapping(target = "modifiedDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "id", expression = "java(request.getId() != null ? request.getId() : java.util.UUID.randomUUID().toString())")
    @Mapping(target = "priority", source = "request", qualifiedByName = "mapPriority")
    Memory toMemory(CreateOrUpdateMemoryRequest request);

    @Named("mapPriority")
    default int mapPriority(CreateOrUpdateMemoryRequest request) {
        return request.getPriority() > 0 ? request.getPriority() : generateRandomPriority();
    }

    default int generateRandomPriority() {
        return (int) (Math.random() * (1000 - 20 + 1)) + 20;
    }

}
