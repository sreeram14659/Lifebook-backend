package com.lifebook.Lifebook.model.responses;

import com.lifebook.Lifebook.model.types.Song;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetSongByIdResponse extends GenericResponse {
    private Song song;
}
