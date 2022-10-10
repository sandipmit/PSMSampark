package com.liminfinite.samparkApp.PSMSampark.models.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@IdClass(CompositeId.class)
public class DistanceMatrix {
    public DistanceMatrix(){}

    public DistanceMatrix(Long karyakarId, Long contactId, Double distance, Double duarationSeconds){
        this.karyakarId = karyakarId;
        this.contactId = contactId;
        this.distance = distance;
        this.duarationSeconds = duarationSeconds;
    }

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id", nullable = false)
//    private Long id;

    @Id
    @Column(name = "karyakar_id")
    private Long karyakarId;

    @Id
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "duration_seconds")
    private Double duarationSeconds;
}
