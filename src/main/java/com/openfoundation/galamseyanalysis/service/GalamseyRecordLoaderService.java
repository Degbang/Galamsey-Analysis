package com.openfoundation.galamseyanalysis.service;

/**
 * Service interface for handling Galamsey record operations.
 */
public interface GalamseyRecordLoaderService {
    /**
     * Reads, validates, and saves all valid Galamsey records into the database.
     * Invalid records are discarded during the process.
     */
    void saveAllGalamseyRecords();
}
