package com.liminfinite.samparkApp.PSMSampark.dao;

import com.liminfinite.samparkApp.PSMSampark.models.entities.Contact;
import com.liminfinite.samparkApp.PSMSampark.models.entities.DistanceMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DistanceMatrixRepository extends JpaRepository<DistanceMatrix, Long>,
        JpaSpecificationExecutor<DistanceMatrix> {
}
