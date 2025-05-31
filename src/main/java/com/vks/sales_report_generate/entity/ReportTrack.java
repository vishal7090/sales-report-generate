package com.vks.sales_report_generate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"fileName", "sheetName"}),
    indexes = {
        @Index(name = "idx_reporttrack_filename", columnList = "fileName"),
        @Index(name = "idx_reporttrack_sheetname", columnList = "sheetName"),
        @Index(name = "idx_reporttrack_status", columnList = "status")
    }
)
@Data
public class ReportTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileName;
    private String status; // e.g., SUCCESS, ERROR
    private LocalDateTime processedAt;
    private String errorMessage;
    @Column(nullable = false)
    private String sheetName;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
} 