package com.openfoundation.galamseyanalysis.util;

import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import com.openfoundation.galamseyanalysis.model.GalamseyRecordDataset;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class for reading Galamsey records from a CSV file.
 */
@Slf4j
public class CsvFileOperation {
    String DEFAULT_OUTPUT_DIRECTORY = "data";

    String DEFAULT_DIRECTORY = "src/main/resources/";

    /**
     * Reads a CSV file from the resources/static folder.
     * @param fileName The name of the CSV file.
     * @return A list of Galamsey records. Returns an empty list if the file is not found.
     */
    public static List<GalamseyRecordDataset> readCsvFile(String fileName, String defaultDirectory) {
        Path filePath = Path.of(defaultDirectory, fileName);

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            return new CsvToBeanBuilder<GalamseyRecordDataset>(reader)
                    .withType(GalamseyRecordDataset.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            log.error("Error loading file from path: {}, Error: {}", filePath, e.getMessage());
        }
        return new ArrayList<>();
    }

    public static File writeCsvFile(List<GalamseyRecordDatasetEntity> galamseyRecordDatasetEntities, String fileName , String defaultFilePath) {

        try {
            Path directoryPath = Path.of(defaultFilePath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(fileName);
            File csvFile = filePath.toFile();

            try (BufferedWriter csvWriter = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                 CSVPrinter csvPrinter = new CSVPrinter(csvWriter, CSVFormat.DEFAULT
                         .builder()
                         .setHeader("City", "Region", "Number_of_Galamsay_Sites")
                         .build())) {

                for (GalamseyRecordDatasetEntity data : galamseyRecordDatasetEntities) {
                    csvPrinter.printRecord(data.getCity(), data.getRegion(), data.getNumberOfGalamseySites());
                }

                log.info("CSV file successfully written at {}", csvFile.getAbsolutePath());
                return csvFile;
            }
        } catch (IOException e) {
            log.error("Error writing CSV file: {}", e.getMessage());
            return null;
        }
    }
}
