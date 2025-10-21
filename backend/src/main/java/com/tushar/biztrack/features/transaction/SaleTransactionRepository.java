package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTransactionRepository extends JpaRepository<SaleTransaction, Long> {
    
    @Query("SELECT DISTINCT t FROM SaleTransaction t LEFT JOIN t.entries WHERE t.orderDate = :date OR t.receivedDate = :date OR (t.orderDate IS NULL OR (t.date > t.orderDate AND t.receivedDate < t.date))")
    List<SaleTransaction> findByDateWithEntries(@Param("date") LocalDate date);
}
