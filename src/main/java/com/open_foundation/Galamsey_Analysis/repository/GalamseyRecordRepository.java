package com.open_foundation.Galamsey_Analysis.repository;

import com.open_foundation.Galamsey_Analysis.entity.GalamseyRecordDatasetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GalamseyRecordRepository extends JpaRepository<GalamseyRecordDatasetEntity, UUID> {

}
