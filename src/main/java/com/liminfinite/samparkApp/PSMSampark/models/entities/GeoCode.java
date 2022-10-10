package com.liminfinite.samparkApp.PSMSampark.models.entities;

import com.liminfinite.samparkApp.PSMSampark.models.LatLong;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "geo_code")
@Data
public class GeoCode {
    public GeoCode(){}

    public  GeoCode(Long personId, LatLong latLong){
        this.personId = personId;
        this.latitude = latLong.getLatitude();
        this.longitude = latLong.getLongitude();
    }

    @Id
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

}
