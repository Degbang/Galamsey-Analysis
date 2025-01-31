package com.openfoundation.galamseyanalysis.repository;

import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository for managing Galamsey record persistence.
 */
public interface GalamseyRecordRepository extends JpaRepository<GalamseyRecordDatasetEntity, UUID> {

}
