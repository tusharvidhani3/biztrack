package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {
    
    @Query("SELECT DISTINCT t FROM PurchaseTransaction t WHERE t.orderDate = :date OR t.receivedDate = :date OR (t.orderDate IS NULL OR (:date > t.orderDate AND t.receivedDate < :date))")
    List<PurchaseTransaction> findByDate(@Param("date") LocalDate date);

}
