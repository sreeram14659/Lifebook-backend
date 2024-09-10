package com.lifebook.Lifebook.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lifebook.Lifebook.model.data.ImageData;
import com.lifebook.Lifebook.model.data.LocationData;
import com.lifebook.Lifebook.model.data.NoteData;
import com.lifebook.Lifebook.model.data.TextData;
import com.lifebook.Lifebook.model.data.VideoData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "dataType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ImageData.class, name = "ImageData"),
    @JsonSubTypes.Type(value = LocationData.class, name = "LocationData"),
    @JsonSubTypes.Type(value = NoteData.class, name = "NoteData"),
    @JsonSubTypes.Type(value = TextData.class, name = "TextData"),
    @JsonSubTypes.Type(value = VideoData.class, name = "VideoData"),
    // Add more subclass mappings here if needed
})
@lombok.Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Data {
    private String dataType;
}