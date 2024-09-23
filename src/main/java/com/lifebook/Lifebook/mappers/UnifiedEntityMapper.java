package com.lifebook.Lifebook.mappers;

import com.lifebook.Lifebook.model.UnifiedEntity;
import com.lifebook.Lifebook.model.types.Memory;
import com.lifebook.Lifebook.model.types.Song;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UnifiedEntityMapper {
    UnifiedEntityMapper INSTANCE = Mappers.getMapper(UnifiedEntityMapper.class);

    Memory toMemory(UnifiedEntity unifiedEntity);
    UnifiedEntity toUnifiedEntity(Memory memory);

    Song toSong(UnifiedEntity unifiedEntity);
    UnifiedEntity toUnifiedEntity(Song memory);
}
