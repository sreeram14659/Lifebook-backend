package com.lifebook.Lifebook.model.responses;

import com.lifebook.Lifebook.model.types.Memory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GetMemoriesResponse  extends GenericResponse {
    private List<Memory> memories;
}