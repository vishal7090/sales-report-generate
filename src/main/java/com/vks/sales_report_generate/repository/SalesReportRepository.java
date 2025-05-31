package com.vks.sales_report_generate.repository;

import com.vks.sales_report_generate.entity.SalesReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SalesReportRepository extends JpaRepository<SalesReport, String> {
    void deleteByFileName(String fileName);

    List<SalesReport> findByDate(LocalDate date);

    List<SalesReport> findByPartyName(String partyName);

    List<SalesReport> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<SalesReport> findByPartyNameAndDateBetween(String partyName, LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT s.partyName FROM SalesReport s")
    List<String> findDistinctPartyNames();

    @Query(value = "SELECT s.party_name, DATE_FORMAT(s.date, '%Y-%m'), SUM(s.total_amount) FROM sales_report s GROUP BY s.party_name, DATE_FORMAT(s.date, '%Y-%m') ORDER BY s.party_name, DATE_FORMAT(s.date, '%Y-%m')", nativeQuery = true)
    List<Object[]> findPartyMonthSalesNative();

    @Query(
            value = "SELECT t.party_name, t.month, t.total_sales, t.percent_change " +
                    "FROM ( " +
                    "  SELECT " +
                    "    s.party_name, " +
                    "    DATE_FORMAT(s.date, '%Y-%m') AS month, " +
                    "    SUM(s.total_amount) AS total_sales, " +
                    "    ROUND(100 * (SUM(s.total_amount) - LAG(SUM(s.total_amount)) OVER (PARTITION BY s.party_name ORDER BY DATE_FORMAT(s.date, '%Y-%m'))) / " +
                    "      NULLIF(LAG(SUM(s.total_amount)) OVER (PARTITION BY s.party_name ORDER BY DATE_FORMAT(s.date, '%Y-%m')), 0), 2) AS percent_change " +
                    "  FROM sales_report s " +
                    "  WHERE s.date IS NOT NULL " +
                    "  GROUP BY s.party_name, month " +
                    ") t " +
                    "ORDER BY t.party_name, t.month",
            nativeQuery = true
    )
    List<Object[]> findPartyMonthSalesWithPercentChangeNative();

    @Query(value = "SELECT s.party_name, SUM(s.total_amount) as totalSales FROM sales_report s GROUP BY s.party_name ORDER BY totalSales DESC", nativeQuery = true)
    List<Object[]> findTopPartySalesNative();

}