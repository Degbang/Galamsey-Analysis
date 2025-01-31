package com.open_foundation.Galamsey_Analysis.service.impl;

import com.open_foundation.Galamsey_Analysis.entity.GalamseyRecordDatasetEntity;
import com.open_foundation.Galamsey_Analysis.model.GalamseyRecordDataset;
import com.open_foundation.Galamsey_Analysis.repository.GalamseyRecordRepository;
import com.open_foundation.Galamsey_Analysis.service.GalamseyRecordLoaderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.open_foundation.Galamsey_Analysis.util.CsvFileReader.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalamsayRecordLoaderServiceImpl implements GalamseyRecordLoaderService {
    @Value("${csv.filePath}")
    private String filePath;
    private final GalamseyRecordRepository galamseyRecordRepository;
    @Override
    public void saveAllGalamseyRecords() {
       var galamseyRecordDatasetEntities = getRecords().stream()
               .map(record -> GalamseyRecordDatasetEntity.builder()
                       .city(record.getCity())
                       .region(record.getRegion())
                       .numberOfGalamseySites(Integer.valueOf(record.getNumberOfGalamseySites()))
                       .build()
               ).toList();
         galamseyRecordRepository.saveAll(galamseyRecordDatasetEntities);
    }
    private List<GalamseyRecordDataset> getRecords() {
        return new ArrayList<>(readCsvFile(filePath).stream()
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


    private boolean isValidRegion(String region){
        var validRegions = Stream.of(
                "Ashanti", "Greater Accra", "Western", "Western North", "Central",
                "Volta", "Oti", "Eastern", "Bono", "Bono East", "Ahafo",
                "Northern", "North East", "Savannah", "Upper East", "Upper West"
        ).map(String::toLowerCase).toList();
        return validRegions.contains(region.toLowerCase());
    }

    private static boolean isValidCity(String city) {
        var lowerCaseCity = city.toLowerCase();
        var invalidWords = List.of("unknown","invalid");
        return !lowerCaseCity.isEmpty() && !invalidWords.contains(lowerCaseCity.trim());
        }

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

        private static boolean hasConsistentGalamseyNumber(List<GalamseyRecordDataset> sitesInGroup) {
            long uniqueGalamseyNumbers = sitesInGroup.stream()
                    .map(GalamseyRecordDataset::getNumberOfGalamseySites)
                    .distinct()
                    .count();

            return uniqueGalamseyNumbers == 1;
        }
}
