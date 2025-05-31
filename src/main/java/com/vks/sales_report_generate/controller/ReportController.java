package com.vks.sales_report_generate.controller;

import com.vks.sales_report_generate.entity.SalesReport;
import com.vks.sales_report_generate.entity.ItemDetail;
import com.vks.sales_report_generate.repository.SalesReportRepository;
import com.vks.sales_report_generate.repository.ItemDetailRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final SalesReportRepository salesReportRepository;
    private final ItemDetailRepository itemDetailRepository;

    @GetMapping("/sales/date")
    @Operation(summary = "Get sales reports by date")
    public List<SalesReport> getSalesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return salesReportRepository.findByDate(date);
    }

    @GetMapping("/sales/party")
    @Operation(summary = "Get sales reports by party name")
    public List<SalesReport> getSalesByParty(@RequestParam String partyName) {
        return salesReportRepository.findByPartyName(partyName);
    }

    @GetMapping("/sales/between")
    @Operation(summary = "Get sales reports between dates")
    public List<SalesReport> getSalesBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return salesReportRepository.findByDateBetween(start, end);
    }

    @GetMapping("/sales/party-between")
    @Operation(summary = "Get sales reports by party name and between dates")
    public List<SalesReport> getSalesByPartyAndBetween(
            @RequestParam String partyName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return salesReportRepository.findByPartyNameAndDateBetween(partyName, start, end);
    }

    @GetMapping("/sales/filter")
    @Operation(summary = "Get sales reports with flexible filters: date, partyName, start, end")
    public List<SalesReport> filterSalesReports(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String partyName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        if (date != null && StringUtils.hasText(partyName)) {
            return salesReportRepository.findByPartyNameAndDateBetween(partyName, date, date);
        } else if (date != null) {
            return salesReportRepository.findByDate(date);
        } else if (StringUtils.hasText(partyName) && start != null && end != null) {
            return salesReportRepository.findByPartyNameAndDateBetween(partyName, start, end);
        } else if (StringUtils.hasText(partyName)) {
            return salesReportRepository.findByPartyName(partyName);
        } else if (start != null && end != null) {
            return salesReportRepository.findByDateBetween(start, end);
        } else {
            return salesReportRepository.findAll();
        }
    }

    @GetMapping("/items/date")
    @Operation(summary = "Get item details by date")
    public List<ItemDetail> getItemsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return itemDetailRepository.findByDate(date);
    }

    @GetMapping("/items/party")
    @Operation(summary = "Get item details by party name")
    public List<ItemDetail> getItemsByParty(@RequestParam String partyName) {
        return itemDetailRepository.findByPartyName(partyName);
    }

    @GetMapping("/items/between")
    @Operation(summary = "Get item details between dates")
    public List<ItemDetail> getItemsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return itemDetailRepository.findByDateBetween(start, end);
    }

    @GetMapping("/items/party-between")
    @Operation(summary = "Get item details by party name and between dates")
    public List<ItemDetail> getItemsByPartyAndBetween(
            @RequestParam String partyName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return itemDetailRepository.findByPartyNameAndDateBetween(partyName, start, end);
    }

    @GetMapping("/sales/party-names")
    @Operation(summary = "Get distinct party names from sales reports")
    public List<String> getDistinctPartyNames() {
        return salesReportRepository.findDistinctPartyNames();
    }

    @GetMapping("/items/product-names")
    @Operation(summary = "Get distinct product (item) names from item details")
    public List<String> getDistinctProductNames() {
        return itemDetailRepository.findDistinctItemNames();
    }
} 