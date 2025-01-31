package com.openfoundation.galamseyanalysis.service.impl;

import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import com.openfoundation.galamseyanalysis.model.GalamseyRecordDataset;
import com.openfoundation.galamseyanalysis.repository.GalamseyRecordRepository;
import com.openfoundation.galamseyanalysis.util.CsvFileOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.util.ReflectionTestUtils.invokeMethod;

@ExtendWith(MockitoExtension.class)
class GalamsayRecordLoaderServiceImplTest {

    @Mock
    private GalamseyRecordRepository galamseyRecordRepository;

    @InjectMocks
    private GalamsayRecordLoaderServiceImpl galamsayRecordLoaderService;

    /**
     * Sets up necessary configurations before each test runs.
     */
    @BeforeEach
    void setUp() {
        setField(galamsayRecordLoaderService, "readerFilePath", "static/test_data.csv");
        setField(galamsayRecordLoaderService, "fileName", "analyzed_data.csv");
        setField(galamsayRecordLoaderService, "writerFilePath", "output");
        setField(galamsayRecordLoaderService, "readFilePath", "static/test_data.csv");
    }

    /**
     * Tests the successful reading, validation, and saving of Galamsey records.
     */
    @Test
    void testSaveAllGalamseyRecords_Success() {
        // Mock Data
        List<GalamseyRecordDataset> mockCsvData = List.of(
                new GalamseyRecordDataset("Obuasi", "Ashanti", "15"),
                new GalamseyRecordDataset("Tarkwa", "Western", "10")
        );

        List<GalamseyRecordDatasetEntity> mockEntityData = List.of(
                new GalamseyRecordDatasetEntity(null, "Obuasi", "Ashanti", 15),
                new GalamseyRecordDatasetEntity(null, "Tarkwa", "Western", 10)
        );

        // Mock static method CsvFileOperation.readCsvFile()
        try (MockedStatic<CsvFileOperation> mockedStatic = mockStatic(CsvFileOperation.class)) {

            // Mock behavior for reading CSV
            mockedStatic.when(() -> CsvFileOperation.readCsvFile(anyString(), anyString()))
                    .thenReturn(mockCsvData);

            // Simulate saving to repository
            when(galamseyRecordRepository.saveAll(anyList())).thenReturn(mockEntityData);

            // Call method under test
            galamsayRecordLoaderService.saveAllGalamseyRecords();

            // Verify interactions
            mockedStatic.verify(() -> CsvFileOperation.readCsvFile(anyString(), anyString()), times(1));
            verify(galamseyRecordRepository, times(1)).saveAll(anyList());
            mockedStatic.verify(() -> CsvFileOperation.writeCsvFile(anyList(), anyString(), anyString()), times(1));
        }
    }

    /**
     * Tests that no records are inserted when the CSV file is empty.
     */
    @Test
    void testSaveAllGalamseyRecords_EmptyCsv_NoInsert() {
        try (MockedStatic<CsvFileOperation> mockedStatic = mockStatic(CsvFileOperation.class)) {

            mockedStatic.when(() -> CsvFileOperation.readCsvFile(anyString(), anyString()))
                    .thenReturn(List.of());

            // Call method under test
            galamsayRecordLoaderService.saveAllGalamseyRecords();

            // Verify no records were saved
            verify(galamseyRecordRepository, never()).saveAll(anyList());
            mockedStatic.verify(() -> CsvFileOperation.writeCsvFile(anyList(), anyString(), anyString()), never());
        }
    }

    /**
     * Tests the validation of a valid and an invalid region.
     */
    @Test
    void testValidRegionCheck() {
        boolean result1 = invokeMethod(galamsayRecordLoaderService, "isValidRegion", "Ashanti");
        boolean result2 = invokeMethod(galamsayRecordLoaderService, "isValidRegion", "InvalidRegion");

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    /**
     * Tests the validation of valid and invalid city names.
     */
    @Test
    void testValidCityCheck() {
        boolean result1 = invokeMethod(galamsayRecordLoaderService, "isValidCity", "Obuasi");
        boolean result2 = invokeMethod(galamsayRecordLoaderService, "isValidCity", "unknown");

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

    /**
     * Tests the validation of the number of Galamsey sites.
     */
    @Test
    void testSanitizeNumberCheck() {
        boolean result1 = invokeMethod(galamsayRecordLoaderService, "sanitizeNumber", "15"); // Valid number
        boolean result2 = invokeMethod(galamsayRecordLoaderService, "sanitizeNumber", "-5"); // Invalid: negative number
        boolean result3 = invokeMethod(galamsayRecordLoaderService, "sanitizeNumber", "abc"); // Invalid: not a number

        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
        assertThat(result3).isFalse();
    }
}
