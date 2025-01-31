package com.openfoundation.galamseyanalysis.service.impl;

import com.openfoundation.galamseyanalysis.dto.GalamseyRecordDto;
import com.openfoundation.galamseyanalysis.dto.GalamseySubRecord;
import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import com.openfoundation.galamseyanalysis.repository.GalamseyRecordRepository;
import com.openfoundation.galamseyanalysis.service.GalamseyRecordRetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalamseyRecordRetrievalServiceImpl implements GalamseyRecordRetrievalService {

    private final GalamseyRecordRepository galamseyRecordRepository;

    @Override
    public List<GalamseyRecordDto> getAllGalamseyRecords() {
        Map<String, List<GalamseySubRecord>> galamseyDataGrouping = getAllRecords().stream()
                .collect(Collectors.groupingBy(
                        GalamseyRecordDatasetEntity::getRegion, Collectors.mapping(data -> GalamseySubRecord.builder()
                                .city(data.getCity())
                                .numberOfGalamseySites(data.getNumberOfGalamseySites())
                                .build(), Collectors.toList())
                ));
        return galamseyDataGrouping.entrySet().stream()
                .map(data -> GalamseyRecordDto.builder()
                        .region(data.getKey())
                        .galamseySubRecords(data.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the total number of recorded Galamsey sites.
     *
     * @return A formatted message indicating the total number of sites.
     */
    @Override
    public String getTotalGalamseySites() {
        var totalSites = getAllRecords()
                .stream()
                .mapToInt(GalamseyRecordDatasetEntity::getNumberOfGalamseySites)
                .sum();
        return String.format("There are %d total Galamsey sites.", totalSites);
    }

    /**
     * Finds the region with the highest number of Galamsey sites.
     *
     * @return A message indicating the region with the most sites.
     */
    @Override
    public String getRegionWithHighestGalamseySites() {
        return getAllRecords()
                .stream()
                .collect(Collectors.groupingBy(
                        GalamseyRecordDatasetEntity::getRegion,
                        Collectors.summingInt(GalamseyRecordDatasetEntity::getNumberOfGalamseySites)
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> String.format("%s has the highest number of Galamsey sites with %d recorded sites.", entry.getKey(), entry.getValue()))
                .orElse("No data available.");
    }

    /**
     * Retrieves a sorted list of cities where the number of Galamsey sites exceeds a given threshold.
     *
     * @param threshold The minimum number of sites required for a city to be included.
     * @return A message listing the cities exceeding the threshold and the count.
     */
    @Override
    public String getCitiesExceedingThreshold(int threshold) {
        List<String> cities = getAllRecords()
                .stream()
                .filter(record -> record.getNumberOfGalamseySites() > threshold)
                .map(GalamseyRecordDatasetEntity::getCity)
                .sorted()
                .collect(Collectors.toList());

        if (cities.isEmpty()) {
            return String.format("No cities have more than %d Galamsey sites.", threshold);
        }
        return String.format("There are %d cities with more than %d Galamsey sites: %s",
                cities.size(), threshold, String.join(", ", cities));
    }

    /**
     * Calculates the average number of Galamsey sites per region and rounds the values.
     *
     * @return A map of regions with their rounded average number of sites.
     */
    @Override
    public Map<String, Integer> getAverageGalamseySitesPerRegion() {
        Map<String, List<Integer>> regionSitesMap = getAllRecords()
                .stream()
                .collect(Collectors.groupingBy(
                        GalamseyRecordDatasetEntity::getRegion,
                        Collectors.mapping(GalamseyRecordDatasetEntity::getNumberOfGalamseySites, Collectors.toList())
                ));

        return regionSitesMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (int) Math.round(entry.getValue().stream()
                                .mapToInt(Integer::intValue)
                                .average()
                                .orElse(0)) // Rounds to the nearest whole number
                ));
    }

    private List<GalamseyRecordDatasetEntity> getAllRecords() {
        return galamseyRecordRepository.findAll();
    }
}