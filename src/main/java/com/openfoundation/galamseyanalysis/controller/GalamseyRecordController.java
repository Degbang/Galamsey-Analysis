package com.openfoundation.galamseyanalysis.controller;

import com.openfoundation.galamseyanalysis.dto.GalamseyRecordDto;
import com.openfoundation.galamseyanalysis.service.GalamseyRecordRetrievalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/galamsey-records")
@RequiredArgsConstructor
@Tag(name = "Galamsey Record API", description = "Endpoints for querying Galamsey site data")
public class GalamseyRecordController {
    private final GalamseyRecordRetrievalService galamseyRecordRetrievalService;

    @Operation(summary = "Get All Galamsey Data", description = "Returns All Galamsey Data")
    @GetMapping("/allGalamseyData")
    public List<GalamseyRecordDto> getAllGalamseyData() {
        return galamseyRecordRetrievalService.getAllGalamseyRecords();
    }

    @Operation(summary = "Get total number of Galamsey sites", description = "Returns the total number of Galamsey sites across all cities")
    @GetMapping("/total-sites")
    public String getTotalGalamseySites() {
        return galamseyRecordRetrievalService.getTotalGalamseySites();
    }

    @Operation(summary = "Get region with highest Galamsey sites", description = "Returns the region with the highest recorded Galamsey sites")
    @GetMapping("/highest-region")
    public String getRegionWithHighestGalamseySites() {
        return galamseyRecordRetrievalService.getRegionWithHighestGalamseySites();
    }

    @Operation(summary = "Get cities exceeding threshold", description = "Returns cities where Galamsey sites exceed a given threshold")
    @GetMapping("/cities-above-threshold")
    public String getCitiesExceedingThreshold(@RequestParam int threshold) {
        return galamseyRecordRetrievalService.getCitiesExceedingThreshold(threshold);
    }

    @Operation(summary = "Get average Galamsey sites per region", description = "Returns the average number of Galamsey sites per region")
    @GetMapping("/average-sites-per-region")
    public Map<String, Integer> getAverageGalamseySitesPerRegion() {
        return galamseyRecordRetrievalService.getAverageGalamseySitesPerRegion();
    }
}
