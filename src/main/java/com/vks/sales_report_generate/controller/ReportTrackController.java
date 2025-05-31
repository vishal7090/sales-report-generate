package com.vks.sales_report_generate.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.vks.sales_report_generate.dto.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vks.sales_report_generate.repository.ItemDetailRepository;
import com.vks.sales_report_generate.repository.SalesReportRepository;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/report-track")
@RequiredArgsConstructor
public class ReportTrackController {

    private final SalesReportRepository salesReportRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    @GetMapping("/summary")
    @Operation(summary = "Get report track summary with top party sales, product-wise sales, and month-on-month growth (parallel execution)")
    public ReportTrackSummaryDTO getReportTrackSummary(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        CompletableFuture<List<PartySalesDTO>> topPartySalesFuture = CompletableFuture.supplyAsync(() -> {
            List<Object[]> raw = salesReportRepository.findTopPartySalesNative();
            return raw.stream().map(arr -> new PartySalesDTO((String) arr[0], ((Number) arr[1]).doubleValue())).collect(Collectors.toList());
        }, executor);
        CompletableFuture<List<PartyProductQuantityDTO>> topPartyProductQuantityFuture = CompletableFuture.supplyAsync(() -> {
            List<Object[]> raw = itemDetailRepository.findTopPartyPerProduct();
            return raw.stream().map(arr -> new PartyProductQuantityDTO((String) arr[0], (String) arr[1], arr[2] != null ? ((Number) arr[2]).doubleValue() : 0, arr[3] != null ? ((Number) arr[3]).doubleValue() : 0)).collect(Collectors.toList());
        }, executor);

        CompletableFuture<List<PartyMonthSalesPercentDTO>> partyMonthSalesPercentFuture = CompletableFuture.supplyAsync(() -> {
            List<Object[]> raw = salesReportRepository.findPartyMonthSalesWithPercentChangeNative();
            return raw.stream().map(arr -> new PartyMonthSalesPercentDTO((String) arr[0], (String) arr[1], arr[2] != null ? ((Number) arr[2]).doubleValue() : 0, arr[3] != null ? ((Number) arr[3]).doubleValue() : 0)).collect(Collectors.toList());
        }, executor);

        CompletableFuture.allOf(topPartySalesFuture, topPartyProductQuantityFuture, partyMonthSalesPercentFuture).join();

        return new ReportTrackSummaryDTO(topPartySalesFuture.join(), topPartyProductQuantityFuture.join(), partyMonthSalesPercentFuture.join());
    }
} 