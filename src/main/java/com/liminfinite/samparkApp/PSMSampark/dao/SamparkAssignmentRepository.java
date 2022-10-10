package com.liminfinite.samparkApp.PSMSampark.dao;

import com.liminfinite.samparkApp.PSMSampark.models.entities.SamparkAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SamparkAssignmentRepository extends JpaRepository<SamparkAssignment, Long>,
        JpaSpecificationExecutor<SamparkAssignment> {
}
