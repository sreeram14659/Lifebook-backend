package com.lifebook.Lifebook.model.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


@lombok.Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonTypeName("VideoData")
public class VideoData extends Data {
    private String videoUrl;

    public VideoData() {
        super("VideoData");
        super.setDataType(VideoData.class.getSimpleName());
    }

    // Constructor with fields
    public VideoData(String id, String videoUrl) {
        super(VideoData.class.getSimpleName());
        this.videoUrl = videoUrl;
    }
}
