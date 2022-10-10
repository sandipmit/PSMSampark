package com.liminfinite.samparkApp.PSMSampark.dao;

import com.liminfinite.samparkApp.PSMSampark.models.entities.Karyakar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface KaryakarRepository extends JpaRepository<Karyakar, Long>,
        JpaSpecificationExecutor<Karyakar> {
}
