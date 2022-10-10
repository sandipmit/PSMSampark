package com.liminfinite.samparkApp.PSMSampark.models.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@Data
@IdClass(CompositeId.class)
public class SamparkAssignment {
    @Id
    @Column(name = "karyakar_id")
    private Long karyakarId;

    @Id
    @Column(name = "contact_id")
    private Long contactId;

    @Column
    private String zone;

    public SamparkAssignment(){}

    public SamparkAssignment(Long karyakarId, Long contactId, String zone){
        this.karyakarId = karyakarId;
        this.contactId = contactId;
        this.zone = zone;
    }
}
