package com.lifebook.Lifebook.model.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@SuperBuilder
@JsonTypeName("TextData")
public class TextData extends Data {
    private String textContent;

    public TextData() {
        super("TextData");
        super.setDataType(TextData.class.getSimpleName());
    }

    // Constructor with fields
    public TextData(String textContent) {
        super(TextData.class.getSimpleName());
        this.textContent = textContent;
    }
}