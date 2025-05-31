package com.vks.sales_report_generate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vks.sales_report_generate.service.ExcelImportService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ExcelImportController {
    private final ExcelImportService excelImportService;
    private final JobLauncher jobLauncher;
    private final Job excelImportJob;

    @PostMapping
    public ResponseEntity<String> importExcelData() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(excelImportJob, params);
            return ResponseEntity.ok("Excel import batch job started.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error starting batch job: " + e.getMessage());
        }
    }
} 