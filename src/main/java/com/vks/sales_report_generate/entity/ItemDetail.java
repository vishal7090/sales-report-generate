package com.vks.sales_report_generate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import jakarta.persistence.Index;

@Entity
@Table(indexes = {
    @Index(name = "idx_itemdetail_filename", columnList = "fileName"),
    @Index(name = "idx_itemdetail_partyname", columnList = "partyName"),
    @Index(name = "idx_itemdetail_date", columnList = "date"),
    @Index(name = "idx_itemdetail_itemname", columnList = "itemName")
})
@Data
public class ItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String invoiceNoTxnNo;
    private String partyName;
    private String itemName;
    private String itemCode;
    private String hsnSac;
    private String category;
    private String count;
    private String challanOrderNo;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal discountPercent;
    private BigDecimal discount;
    private BigDecimal taxPercent;
    private BigDecimal tax;
    private String transactionType;
    private BigDecimal amount;
    @Column(nullable = false)
    private String fileName;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;
} 