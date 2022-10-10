package com.liminfinite.samparkApp.PSMSampark.models;

import lombok.Data;

@Data
public class LatLong {
    private Double latitude;
    private Double longitude;

    public LatLong(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
