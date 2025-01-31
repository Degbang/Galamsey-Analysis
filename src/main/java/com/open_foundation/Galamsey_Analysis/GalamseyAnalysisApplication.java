package com.open_foundation.Galamsey_Analysis;

import com.open_foundation.Galamsey_Analysis.service.GalamseyRecordLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class GalamseyAnalysisApplication implements ApplicationRunner {
	private final GalamseyRecordLoaderService galamseyRecordLoaderService;

	public static void main(String[] args) {
		SpringApplication.run(GalamseyAnalysisApplication.class, args);
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Starting Database Inserts...");
		galamseyRecordLoaderService.saveAllGalamseyRecords();
		log.info("Database Inserts Done...");
	}
}
