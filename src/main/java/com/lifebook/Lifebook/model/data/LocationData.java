package com.lifebook.Lifebook.model.data;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.lifebook.Lifebook.model.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@lombok.Data
@SuperBuilder
@JsonTypeName("LocationData")
public class LocationData extends Data {
    private double latitude;
    private double longitude;

    public LocationData() {
        super("LocationData");
    }

    // Constructor with fields
    public LocationData(double latitude, double longitude) {
        super(LocationData.class.getSimpleName());
        this.latitude = latitude;
        this.longitude = longitude;
    }
}