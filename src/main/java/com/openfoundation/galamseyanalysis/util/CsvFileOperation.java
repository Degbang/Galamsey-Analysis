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
    private static final String DEFAULT_DIRECTORY = "data";

    /**
     * Reads a CSV file and converts it into a list of {@link GalamseyRecordDataset} objects.
     *
     * @param filePath The path to the CSV file.
     * @return A list of Galamsey records. Returns an empty list if the file is not found.
     */
    public static List<GalamseyRecordDataset> readCsvFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            Reader reader = new InputStreamReader(resource.getInputStream());

            List<GalamseyRecordDataset> data = new CsvToBeanBuilder<GalamseyRecordDataset>(reader)
                    .withType(GalamseyRecordDataset.class)
                    .build()
                    .parse();

            return data;
        } catch (Exception e) {
            log.error("Error loading file from classpath: {}, Error: {}", filePath, e.getMessage());
        }
        return new ArrayList<>();
    }

    public static File writeCsvFile(List<GalamseyRecordDatasetEntity> galamseyRecordDatasetEntities, String fileName) {

        try {
            Path directoryPath = Path.of(DEFAULT_DIRECTORY);
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
