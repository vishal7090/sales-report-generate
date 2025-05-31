package com.vks.sales_report_generate.service.processor;

import java.util.Iterator;
import java.util.List;

import com.vks.sales_report_generate.util.ExcelCellUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.stereotype.Component;

import com.vks.sales_report_generate.entity.ItemDetail;
import com.vks.sales_report_generate.repository.ItemDetailRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemDetailsSheetProcessor implements SheetProcessor {
    private final ItemDetailRepository itemDetailRepository;

    @Override
    public void process(Sheet sheet, String fileName) {
        if (sheet == null) {
            log.warn("Item Details sheet not found");
            return;
        }
        List<String> expectedHeaders = List.of(
            "Date", "Invoice No./Txn No.", "Party Name", "Item Name", "Item Code", "HSN/SAC", "Category", "Count", "Challan/Order No.", "Quantity", "Unit", "UnitPrice", "Discount Percent", "Discount", "Tax Percent", "Tax", "Transaction Type", "Amount"
        );
        int headerRowIndex = -1;
        for (Row row : sheet) {
            if (isHeaderRow(row, expectedHeaders)) {
                headerRowIndex = row.getRowNum();
                break;
            }
        }
        if (headerRowIndex == -1) {
            log.warn("Header row not found in Item Details sheet!");
            return;
        }
        itemDetailRepository.deleteByFileName(fileName);
        for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;
            try {
                int col = 0;
                ItemDetail id = new ItemDetail();
                id.setDate(ExcelCellUtil.getLocalDate(row.getCell(col++)));
                id.setInvoiceNoTxnNo(ExcelCellUtil.getString(row.getCell(col++)));
                id.setPartyName(ExcelCellUtil.getString(row.getCell(col++)));
                id.setItemName(ExcelCellUtil.getString(row.getCell(col++)));
                id.setItemCode(ExcelCellUtil.getString(row.getCell(col++)));
                id.setHsnSac(ExcelCellUtil.getString(row.getCell(col++)));
                id.setCategory(ExcelCellUtil.getString(row.getCell(col++)));
                id.setCount(ExcelCellUtil.getString(row.getCell(col++)));
                id.setChallanOrderNo(ExcelCellUtil.getString(row.getCell(col++)));
                id.setQuantity(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setUnit(ExcelCellUtil.getString(row.getCell(col++)));
                id.setUnitPrice(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setDiscountPercent(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setDiscount(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setTaxPercent(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setTax(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setTransactionType(ExcelCellUtil.getString(row.getCell(col++)));
                id.setAmount(ExcelCellUtil.getBigDecimal(row.getCell(col++)));
                id.setFileName(fileName);
                itemDetailRepository.save(id);
            } catch (Exception e) {
                log.error("Error parsing Item Details row {}: {}", row.getRowNum(), e.getMessage());
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
        return "Item Details";
    }
} 