package com.open_foundation.Galamsey_Analysis.util;

import com.open_foundation.Galamsey_Analysis.model.GalamseyRecordDataset;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CsvFileReader {
    public static List<GalamseyRecordDataset> readCsvFile(String filePath){
        try {
            List<GalamseyRecordDataset> data = new CsvToBeanBuilder(new FileReader(filePath))
                    .withType(GalamseyRecordDataset.class)
                    .build()
                    .parse();
            return data;
        } catch (FileNotFoundException e) {
            log.error("Error Finding File at {} with error message {}",filePath, e.getMessage());
        }
        return new ArrayList<>();
    }

}
