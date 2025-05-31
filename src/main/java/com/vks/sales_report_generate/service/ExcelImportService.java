package com.vks.sales_report_generate.service;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.vks.sales_report_generate.config.ExcelImportProperties;
import com.vks.sales_report_generate.entity.ReportTrack;
import com.vks.sales_report_generate.repository.ReportTrackRepository;
import com.vks.sales_report_generate.service.processor.SheetProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelImportService {

    private final ReportTrackRepository reportTrackRepository;
    private final ExcelImportProperties excelImportProperties;
    private final List<SheetProcessor> sheetProcessors;

    public void importData() {
        importData((File) null);
    }

    public void importData(File file) {
        if (file != null) {
            processFile(file);
            return;
        }
        String excelFolderPath = excelImportProperties.getFolderPath();
        File folder = new File(excelFolderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));
        if (files == null || files.length == 0) {
            log.warn("No Excel files found in folder: {}", excelFolderPath);
            return;
        }
        for (File f : files) {
            processFile(f);
        }
    }

    public void importData(String filePath) {
        if (filePath != null && !filePath.isBlank()) {
            importData(new File(filePath));
        } else {
            importData((File) null);
        }
    }

    private void processFile(File file) {
        String fileName = file.getName();
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            log.info("Processing file: {}", fileName);
            for (SheetProcessor processor : sheetProcessors) {
                String sheetName = processor.getSheetName();
                Sheet sheet = workbook.getSheet(sheetName);
                Optional<ReportTrack> existing = reportTrackRepository.findByFileNameAndSheetName(fileName, sheetName);
                if (existing.isPresent() && "SUCCESS".equals(existing.get().getStatus())) {
                    log.info("File {} sheet {} already processed successfully. Skipping.", fileName, sheetName);
                    continue;
                }
                ReportTrack track = existing.orElseGet(ReportTrack::new);
                track.setFileName(fileName);
                track.setSheetName(sheetName);
                track.setProcessedAt(LocalDateTime.now());
                try {
                    if (sheet != null) {
                        log.info("Processing sheet: {}", sheetName);
                        processor.process(sheet, fileName);
                        track.setStatus("SUCCESS");
                        track.setErrorMessage(null);
                    } else {
                        log.warn("Sheet '{}' not found in file {}", sheetName, fileName);
                        track.setStatus("NOT_FOUND");
                        track.setErrorMessage("Sheet not found");
                    }
                } catch (Exception e) {
                    log.error("Error processing file {} sheet {}: {}", fileName, sheetName, e.getMessage(), e);
                    track.setStatus("ERROR");
                    track.setErrorMessage(e.getMessage());
                }
                track.setProcessedAt(LocalDateTime.now());
                reportTrackRepository.save(track);
            }
        } catch (Exception e) {
            log.error("Error processing file {}: {}", fileName, e.getMessage(), e);
        }
    }

    private String getString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((long) cell.getNumericCellValue());
        return null;
    }

    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return BigDecimal.valueOf(cell.getNumericCellValue());
        if (cell.getCellType() == CellType.STRING) {
            try {
                return new BigDecimal(cell.getStringCellValue());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private LocalDate getLocalDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
} 