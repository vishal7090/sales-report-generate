package com.vks.sales_report_generate.dto;

import java.util.List;

public class ReportTrackSummaryDTO {
    private List<PartySalesDTO> topPartySales;
    private List<PartyProductQuantityDTO> topPartyProductQuantity;
    private List<PartyMonthSalesPercentDTO> partyMonthSalesPercent;

    public ReportTrackSummaryDTO(List<PartySalesDTO> topPartySales, List<PartyProductQuantityDTO> topPartyProductQuantity, List<PartyMonthSalesPercentDTO> partyMonthSalesPercent) {
        this.topPartySales = topPartySales;
        this.topPartyProductQuantity = topPartyProductQuantity;
        this.partyMonthSalesPercent = partyMonthSalesPercent;
    }

    public List<PartySalesDTO> getTopPartySales() {
        return topPartySales;
    }


    public List<PartyProductQuantityDTO> getTopPartyProductQuantity() {
        return topPartyProductQuantity;
    }


    public List<PartyMonthSalesPercentDTO> getpartyMonthSalesPercent() {
        return partyMonthSalesPercent;
    }
} 