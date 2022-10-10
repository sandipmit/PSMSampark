package com.liminfinite.samparkApp.PSMSampark.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Karyakar {
    @Id
    @Column(name = "person_id", nullable = false)
    private Long personId;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "address_line")
    private String addressLine;
    @Column(name = "city")
    private String city;
    @Column(name = "zip_code")
    private String zipCode;
    @Column(name = "state")
    private String State;

    @Column(name = "zone")
    private String zone;

    @Column(name = "assignments")
    private Integer assignments;

    @Column(name = "is_active")
    private String isActive;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private GeoCode geoCode;
}
