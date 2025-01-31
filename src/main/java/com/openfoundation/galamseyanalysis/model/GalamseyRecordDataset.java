package com.openfoundation.galamseyanalysis.model;

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
    @CsvBindByName(column = "City") // Maps to 'City' column in CSV
    private String city;
    @CsvBindByName(column = "Region") // Maps to 'Region' column in CSV
    private String region;
    @CsvBindByName(column = "Number_of_Galamsay_Sites") // Maps to 'Number_of_Galamsay_Sites' column in CSV
    private String numberOfGalamseySites;
}


