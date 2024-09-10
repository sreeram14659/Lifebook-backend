package com.lifebook.Lifebook.model.responses;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class GenericResponse {
    private String status;
    private String message;
}