package com.open_foundation.Galamsey_Analysis.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class GalamseyRecordDataset {
    @CsvBindByName(column = "City")
    private String city;
    @CsvBindByName(column = "Region")
    private String  region;
    @CsvBindByName(column = "Number_of_Galamsay_Sites")
    private String numberOfGalamseySites;
}


