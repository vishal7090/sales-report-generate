package com.vks.sales_report_generate.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import com.vks.sales_report_generate.service.ExcelImportService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelImportTasklet implements Tasklet {

    private final ExcelImportService excelImportService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        String filePath = (String) chunkContext.getStepContext()
            .getJobParameters()
            .get("filePath");
        if (filePath != null && !filePath.isBlank()) {
            log.info("Batch task processing specific file: {}", filePath);
            excelImportService.importData(filePath);
        } else {
            log.info("Batch task processing all files in folder.");
            excelImportService.importData();
        }
        log.info("Excel import batch task completed.");
        return RepeatStatus.FINISHED;
    }
} 