package com.liminfinite.samparkApp.PSMSampark.models.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class CompositeId implements Serializable {
    @Column(name = "karyakar_id")
    private Long karyakarId;

    @Column(name = "contact_id")
    private Long contactId;

    public CompositeId(){}

    public CompositeId(Long karyakarId, Long contactId){
        this.karyakarId = karyakarId;
        this.contactId = contactId;
    }
}
