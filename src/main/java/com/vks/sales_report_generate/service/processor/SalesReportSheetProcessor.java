package com.vks.sales_report_generate.service.processor;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;

import com.vks.sales_report_generate.entity.SalesReport;
import com.vks.sales_report_generate.repository.SalesReportRepository;
import com.vks.sales_report_generate.util.ExcelCellUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesReportSheetProcessor implements SheetProcessor {
    private final SalesReportRepository salesReportRepository;

    @Override
    public void process(Sheet sheet, String fileName) {
        if (sheet == null) {
            log.warn("Sales Report sheet not found");
            return;
        }
        List<String> expectedHeaders = List.of("Date", "Invoice No", "Party Name", "Transaction Type", "Total Amount", "Payment Type", "Received/Paid Amount", "Balance Due");
        int headerRowIndex = -1;
        for (Row row : sheet) {
            if (isHeaderRow(row, expectedHeaders)) {
                headerRowIndex = row.getRowNum();
                break;
            }
        }
        if (headerRowIndex == -1) {
            log.warn("Header row not found in Sales Report sheet!");
            return;
        }
        salesReportRepository.deleteByFileName(fileName);
        for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;
            try {
                int col = 0;
                SalesReport sr = new SalesReport();
                sr.setDate(ExcelCellUtil.getLocalDate(row.getCell(col++)));
                sr.setInvoiceNo(ExcelCellUtil.getString(row.getCell(col++)));
                sr.setPartyName(ExcelCellUtil.getString(row.getCell(col++)));
                sr.setTransactionType(ExcelCellUtil.getString(row.getCell(col++)));
                sr.setTotalAmount(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                sr.setPaymentType(ExcelCellUtil.getString(row.getCell(col++)));
                sr.setReceivedPaidAmount(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                sr.setBalanceDue(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                sr.setFileName(fileName);
                if(sr.getInvoiceNo() != null && !sr.getInvoiceNo().isEmpty()) {
                    salesReportRepository.save(sr);
                }
            } catch (Exception e) {
                log.error("Error parsing Sales Report row {}: {}", row.getRowNum(), e.getMessage());
            }
        }
    }

    private boolean isHeaderRow(Row row, List<String> expectedHeaders) {
        for (int i = 0; i < expectedHeaders.size(); i++) {
            Cell cell = row.getCell(i);
            if (cell == null || cell.getCellType() != CellType.STRING || !expectedHeaders.get(i).equalsIgnoreCase(cell.getStringCellValue().trim())) {
                return false;
            }
        }
        return true;
    }

    private boolean isRowEmpty(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getSheetName() {
        return "Sale Report";
    }
} 