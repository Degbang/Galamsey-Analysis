package com.openfoundation.galamseyanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalamseySubRecord {
    private String city;
    private Integer numberOfGalamseySites;
}
