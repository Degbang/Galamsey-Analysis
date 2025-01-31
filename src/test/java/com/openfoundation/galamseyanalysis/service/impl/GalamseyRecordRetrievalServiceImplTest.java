package com.openfoundation.galamseyanalysis.service.impl;

import com.openfoundation.galamseyanalysis.dto.GalamseyRecordDto;
import com.openfoundation.galamseyanalysis.entity.GalamseyRecordDatasetEntity;
import com.openfoundation.galamseyanalysis.repository.GalamseyRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalamseyRecordRetrievalServiceImplTest {

    @Mock
    private GalamseyRecordRepository galamseyRecordRepository;

    @InjectMocks
    private GalamseyRecordRetrievalServiceImpl galamseyRecordRetrievalService;

    private List<GalamseyRecordDatasetEntity> mockData;

    /**
     * Sets up mock test data before each test case runs.
     */
    @BeforeEach
    void setUp() {
        mockData = List.of(
                new GalamseyRecordDatasetEntity(null, "Obuasi", "Ashanti", 15),
                new GalamseyRecordDatasetEntity(null, "Tarkwa", "Western", 10),
                new GalamseyRecordDatasetEntity(null, "Tema", "Greater Accra", 25),
                new GalamseyRecordDatasetEntity(null, "Kumasi", "Ashanti", 20),
                new GalamseyRecordDatasetEntity(null, "Takoradi", "Western", 30)
        );
    }

    /**
     * Tests retrieval of all Galamsey records grouped by region.
     */
    @Test
    void testGetAllGalamseyRecords() {
        when(galamseyRecordRepository.findAll()).thenReturn(mockData);

        List<GalamseyRecordDto> result = galamseyRecordRetrievalService.getAllGalamseyRecords();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.stream().map(GalamseyRecordDto::getRegion))
                .containsExactlyInAnyOrder("Ashanti", "Western", "Greater Accra");

        verify(galamseyRecordRepository, times(1)).findAll();
    }

    /**
     * Tests retrieval of the total number of Galamsey sites.
     */
    @Test
    void testGetTotalGalamseySites() {
        when(galamseyRecordRepository.findAll()).thenReturn(mockData);

        String result = galamseyRecordRetrievalService.getTotalGalamseySites();
        assertThat(result).isEqualTo("There are 100 total Galamsey sites.");

        verify(galamseyRecordRepository, times(1)).findAll();
    }

    /**
     * Tests retrieval of the region with the highest number of Galamsey sites.
     */
    @Test
    void testGetRegionWithHighestGalamseySites() {
        when(galamseyRecordRepository.findAll()).thenReturn(mockData);

        String result = galamseyRecordRetrievalService.getRegionWithHighestGalamseySites();
        assertThat(result).isEqualTo("Western has the highest number of Galamsey sites with 40 recorded sites.");

        verify(galamseyRecordRepository, times(1)).findAll();
    }

    /**
     * Tests retrieval of cities where Galamsey site count exceeds a given threshold.
     */
    @Test
    void testGetCitiesExceedingThreshold() {
        when(galamseyRecordRepository.findAll()).thenReturn(mockData);

        String result = galamseyRecordRetrievalService.getCitiesExceedingThreshold(15);
        assertThat(result).isEqualTo("There are 3 cities with more than 15 Galamsey sites: Kumasi, Takoradi, Tema");

        verify(galamseyRecordRepository, times(1)).findAll();
    }

    /**
     * Tests calculation of the average number of Galamsey sites per region.
     */
    @Test
    void testGetAverageGalamseySitesPerRegion() {
        when(galamseyRecordRepository.findAll()).thenReturn(mockData);

        Map<String, Integer> result = galamseyRecordRetrievalService.getAverageGalamseySitesPerRegion();

        assertThat(result)
                .containsEntry("Ashanti", 18)
                .containsEntry("Western", 20)
                .containsEntry("Greater Accra", 25);

        verify(galamseyRecordRepository, times(1)).findAll();
    }
}
