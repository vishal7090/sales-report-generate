package com.vks.sales_report_generate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
    @Index(name = "idx_salesreport_filename", columnList = "fileName"),
    @Index(name = "idx_salesreport_partyname", columnList = "partyName"),
    @Index(name = "idx_salesreport_date", columnList = "date")
})
@Data
public class SalesReport {
    @Id
    private String invoiceNo;
    private LocalDate date;
    private String partyName;
    private String transactionType;
    private BigDecimal totalAmount;
    private String paymentType;
    private BigDecimal receivedPaidAmount;
    private BigDecimal balanceDue;
    @Column(nullable = false)
    private String fileName;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
} 