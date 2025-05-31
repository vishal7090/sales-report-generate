package com.vks.sales_report_generate.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ExcelBatchConfig {

    private final ExcelImportTasklet excelImportTasklet;

    @Bean
    public Job excelImportJob(JobRepository jobRepository, Step excelImportStep) {
        return new JobBuilder("excelImportJob", jobRepository)
                .start(excelImportStep)
                .build();
    }

    @Bean
    public Step excelImportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("excelImportStep", jobRepository)
                .tasklet(excelImportTasklet, transactionManager)
                .build();
    }
} 