package com.lifebook.Lifebook.model.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@SuperBuilder
@JsonTypeName("ImageData")
public class ImageData extends Data {
    private String imageUrl;

    // Default constructor
    public ImageData() {
        super(ImageData.class.getSimpleName());
    }

    // Constructor with fields
    public ImageData(String imageUrl) {
        super(ImageData.class.getSimpleName());
        this.imageUrl = imageUrl;
    }
}
