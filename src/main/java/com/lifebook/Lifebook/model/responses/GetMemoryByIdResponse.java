package com.lifebook.Lifebook.model.responses;

import com.lifebook.Lifebook.model.types.Memory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetMemoryByIdResponse extends GenericResponse {
    private Memory memory;
}
