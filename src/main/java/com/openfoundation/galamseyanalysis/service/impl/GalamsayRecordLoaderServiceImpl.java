package com.openfoundation.galamseyanalysis.service.impl;

import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import com.openfoundation.galamseyanalysis.model.GalamseyRecordDataset;
import com.openfoundation.galamseyanalysis.repository.GalamseyRecordRepository;
import com.openfoundation.galamseyanalysis.service.GalamseyRecordLoaderService;
import com.openfoundation.galamseyanalysis.util.CsvFileOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalamsayRecordLoaderServiceImpl implements GalamseyRecordLoaderService {
    private final GalamseyRecordRepository galamseyRecordRepository;
    @Value("${csv.filePath.reader}")
    private String readerFilePath;
    @Value("${csv.filePath.fileName}")
    private String fileName;

    /**
     * Validates a city. checks if a city is null/empty or contains "unknown" or "invalid"
     * If false, the entire row is considered invalid.
     *
     * @param city The Galamsey record to validate.
     */
    private static boolean isValidCity(String city) {
        var lowerCaseCity = city.toLowerCase();
        var invalidWords = List.of("unknown", "invalid");
        return !lowerCaseCity.isEmpty() && !invalidWords.contains(lowerCaseCity.trim());
    }

    /**
     * Ensures all records in a group have consistent Galamsey site counts.
     *
     * @param sitesInGroup List of Galamsey records.
     * @return true if all records have the same site count, false otherwise.
     */
    private static boolean hasConsistentGalamseyNumber(List<GalamseyRecordDataset> sitesInGroup) {
        long uniqueGalamseyNumbers = sitesInGroup.stream()
                .map(GalamseyRecordDataset::getNumberOfGalamseySites)
                .distinct()
                .count();
        return uniqueGalamseyNumbers == 1;
    }

    /**
     * Reads, validates, and saves only cleaned Galamsey records into the database and csv file.
     */
    @Override
    public void saveAllGalamseyRecords() {
        List<GalamseyRecordDataset> records = getRecords();

        if (records.isEmpty()) {
            log.error("No valid records found. Skipping database insertion.");
            return;
        }
        var galamseyRecordDatasetEntities = records.stream()
                .map(record -> GalamseyRecordDatasetEntity.builder()
                        .city(record.getCity())
                        .region(record.getRegion())
                        .numberOfGalamseySites(Integer.valueOf(record.getNumberOfGalamseySites()))
                        .build()
                ).toList();
        log.info("Starting Database Inserts...");
        galamseyRecordRepository.saveAll(galamseyRecordDatasetEntities);
        log.info("{} records successfully inserted into the database.", galamseyRecordDatasetEntities.size());
        log.info("Saving Analyzed File");
        CsvFileOperation.writeCsvFile(galamseyRecordDatasetEntities, fileName);
    }

    /**
     * Reads and processes records from the CSV file while ensuring data validation and uniqueness.
     * If the file is missing or unreadable, an empty list is returned.
     * Invalid records are removed.
     *
     * @return List of valid Galamsey records
     */
    private List<GalamseyRecordDataset> getRecords() {
        List<GalamseyRecordDataset> csvRecords = CsvFileOperation.readCsvFile(readerFilePath);

        if (csvRecords == null || csvRecords.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(csvRecords.stream()
                .filter(data -> isValidRegion(data.getRegion()))
                .filter(data -> isValidCity(data.getCity()))
                .filter(data -> sanitizeNumber(data.getNumberOfGalamseySites()))
                .collect(Collectors.groupingBy(data -> data.getRegion() + "-" + data.getCity()))
                .values().stream()
                .filter(GalamsayRecordLoaderServiceImpl::hasConsistentGalamseyNumber)
                .flatMap(List::stream)
                .collect(Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(GalamseyRecordDataset::getRegion)
                                .thenComparing(GalamseyRecordDataset::getCity)))));
    }

    /**
     * Validates a region. Checks if a region is included in the list of regions in Ghana.
     * If false, the entire row is considered invalid.
     *
     * @param region The Galamsey record to validate.
     */
    private boolean isValidRegion(String region) {
        var validRegions = Stream.of(
                "Ashanti", "Greater Accra", "Western", "Western North", "Central",
                "Volta", "Oti", "Eastern", "Bono", "Bono East", "Ahafo",
                "Northern", "North East", "Savannah", "Upper East", "Upper West"
        ).map(String::toLowerCase).toList();
        return validRegions.contains(region.toLowerCase());
    }

    /**
     * Validates the numberOfGalamseySites. checks if the number of site is a positive integer.
     * If false, the entire row is considered invalid.
     *
     * @param value The Galamsey record to validate.
     */
    public boolean sanitizeNumber(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
