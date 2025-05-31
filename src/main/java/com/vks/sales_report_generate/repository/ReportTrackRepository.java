package com.vks.sales_report_generate.repository;

import com.vks.sales_report_generate.entity.ReportTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportTrackRepository extends JpaRepository<ReportTrack, Long> {
    Optional<ReportTrack> findByFileNameAndSheetName(String fileName, String sheetName);

    void deleteByFileNameAndSheetName(String fileName, String sheetName);

    @Query("SELECT DISTINCT rt.fileName FROM ReportTrack rt")
    List<String> findDistinctFileNames();
} 