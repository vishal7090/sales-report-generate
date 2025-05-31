package com.vks.sales_report_generate.watcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.vks.sales_report_generate.config.ExcelImportProperties;
import com.vks.sales_report_generate.service.ExcelImportService;

import java.io.IOException;
import java.nio.file.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExcelFolderWatcher {

    private final ExcelImportProperties excelImportProperties;
    private final JobLauncher jobLauncher;
    private final Job excelImportJob;
    private final ExcelImportService excelImportService;

    @EventListener(ApplicationReadyEvent.class)
    public void watchFolder() {
        String folder = excelImportProperties.getFolderPath();
        if (folder == null || folder.isBlank()) {
            log.error("Excel folder path is not set! Please set 'excel.folder-path' in application.properties.");
            return;
        }
        Path folderPath;
        try {
            folderPath = Paths.get(folder);
        } catch (Exception e) {
            log.error("Error resolving Excel folder path: {}", e.getMessage(), e);
            return;
        }
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            log.info("Watching folder for new Excel files: {}", folderPath);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    Path fileName = (Path) event.context();
                    if (kind == StandardWatchEventKinds.ENTRY_CREATE && fileName.toString().endsWith(".xlsx")) {
                        log.info("New Excel file detected: {}", fileName);
                        triggerBatchJob(folderPath.resolve(fileName).toString());
                    }
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY && fileName.toString().endsWith(".xlsx")) {
                        log.info("Update Excel file detected: {}", fileName);
                        triggerBatchJob(folderPath.resolve(fileName).toString());
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error watching folder: {}", e.getMessage(), e);
        }
    }

    private void triggerBatchJob(String filePath) {
        try {
            JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).addString("filePath", filePath).toJobParameters();
            jobLauncher.run(excelImportJob, params);
            log.info("Excel import batch job started automatically for file: {}.", filePath);
        } catch (Exception e) {
            log.error("Error starting batch job: {}", e.getMessage(), e);
        }
    }
} 