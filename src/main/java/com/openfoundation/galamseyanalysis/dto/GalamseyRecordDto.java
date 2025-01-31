package com.openfoundation.galamseyanalysis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GalamseyRecordDto {
    private String region;
    private List<GalamseySubRecord> galamseySubRecords;
}
