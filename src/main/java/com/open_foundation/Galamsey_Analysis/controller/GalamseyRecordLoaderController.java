package com.open_foundation.Galamsey_Analysis.controller;

import com.open_foundation.Galamsey_Analysis.model.GalamseyRecordDataset;
import com.open_foundation.Galamsey_Analysis.service.GalamseyRecordLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GalamseyRecordLoaderController {
    private final GalamseyRecordLoaderService galamseyRecordLoaderService;
}

