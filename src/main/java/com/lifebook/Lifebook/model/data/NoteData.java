package com.lifebook.Lifebook.model.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@SuperBuilder
@JsonTypeName("NoteData")
public class NoteData extends Data {
    private String notes;

    public NoteData() {
        super();
        super.setDataType(NoteData.class.getSimpleName());
    }

    // Constructor with fields
    public NoteData(String notes) {
        super(NoteData.class.getSimpleName());
        this.notes = notes;
    }
}