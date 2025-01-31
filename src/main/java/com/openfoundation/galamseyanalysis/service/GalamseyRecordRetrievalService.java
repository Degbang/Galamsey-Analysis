package com.openfoundation.galamseyanalysis.service;


import com.openfoundation.galamseyanalysis.dto.GalamseyRecordDto;

import java.util.List;
import java.util.Map;

/**
 * Service interface for querying Galamsey records stored in the database.
 * Provides methods to retrieve key insights into illegal small-scale mining activities.
 */
public interface GalamseyRecordRetrievalService {
    List<GalamseyRecordDto> getAllGalamseyRecords();

    String getTotalGalamseySites();

    String getRegionWithHighestGalamseySites();

    String getCitiesExceedingThreshold(int threshold);

    Map<String, Integer> getAverageGalamseySitesPerRegion();
}
