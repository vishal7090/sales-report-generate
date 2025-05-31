package com.vks.sales_report_generate.service.processor;

import org.apache.poi.ss.usermodel.Sheet;

public interface SheetProcessor {
    /**
     * @param sheet the Excel sheet to process
     * @param fileName the name of the file being processed
     */
    void process(Sheet sheet, String fileName);
    /**
     * @return the name of the sheet this processor handles
     */
    String getSheetName();
} 