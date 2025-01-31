package com.openfoundation.galamseyanalysis.util;

import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import com.openfoundation.galamseyanalysis.model.GalamseyRecordDataset;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvFileOperationTest {

    /**
     * Tests the functionality of reading a CSV file.
     * Ensures that the method correctly parses CSV data into Java objects.
     */
    @Test
    void testReadCsvFile() {
        // Define the test directory and file name
        String testDirectory = "src/test/resources/TestFiles/";
        String testFileName = "csvTestFile.csv";

        // Invoke the method to read the CSV file
        List<GalamseyRecordDataset> result = CsvFileOperation.readCsvFile(testFileName, testDirectory);

        // Expected data that should be returned
        List<GalamseyRecordDataset> expectedData = List.of(
                new GalamseyRecordDataset("Obuasi", "Ashanti", "15"),
                new GalamseyRecordDataset("Tarkwa", "Western", "10")
        );

        // Assert that the parsed data matches the expected data
        assertThat(result).usingRecursiveFieldByFieldElementComparator().isEqualTo(expectedData);
    }

    /**
     * Tests the functionality of writing data to a CSV file.
     * Ensures that a file is successfully created in the specified location.
     */
    @Test
    void testWriteCsvFile() {
        // Define the output file path and name
        String defaultFilePath = "src/test/resources/TestFiles/";
        String testFileName = "output.csv";

        // Sample data to be written to the CSV file
        List<GalamseyRecordDatasetEntity> datasetEntities = List.of(
                new GalamseyRecordDatasetEntity(null, "Accra", "Greater Accra", 20),
                new GalamseyRecordDatasetEntity(null, "Kumasi", "Ashanti", 25)
        );

        // Invoke the method to write the CSV file
        File writtenFile = CsvFileOperation.writeCsvFile(datasetEntities, testFileName, defaultFilePath);

        // Assert that the file was successfully created
        assertThat(writtenFile).exists();
    }
}
