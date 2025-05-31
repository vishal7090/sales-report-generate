package com.vks.sales_report_generate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import com.vks.sales_report_generate.entity.ItemDetail;
import com.vks.sales_report_generate.dto.PartyProductQuantityDTO;

import java.time.LocalDate;
import java.util.List;

public interface ItemDetailRepository extends JpaRepository<ItemDetail, Long> {
    void deleteByFileName(String fileName);

    List<ItemDetail> findByDate(LocalDate date);

    List<ItemDetail> findByPartyName(String partyName);

    List<ItemDetail> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<ItemDetail> findByPartyNameAndDateBetween(String partyName, LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT i.itemName FROM ItemDetail i")
    List<String> findDistinctItemNames();

    @Query(
            value = "SELECT party_name, item_name, totalQuantity, totalAmount FROM (" +
                    "  SELECT i.party_name, i.item_name, SUM(i.quantity) AS totalQuantity, SUM(i.amount) AS totalAmount, " +
                    "         ROW_NUMBER() OVER (PARTITION BY i.item_name ORDER BY SUM(i.quantity) DESC) as rn " +
                    "  FROM item_detail i " +
                    "  GROUP BY i.party_name, i.item_name" +
                    ") ranked WHERE rn = 1 ORDER BY totalQuantity DESC",
            nativeQuery = true
    )
    List<Object[]> findTopPartyPerProduct();

}