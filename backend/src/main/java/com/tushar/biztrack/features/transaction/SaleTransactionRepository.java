package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTransactionRepository extends JpaRepository<SaleTransaction, Long> {
    
    @Query("SELECT DISTINCT t FROM SaleTransaction t WHERE t.orderDate = :date OR t.dispatchDate = :date OR (t.orderDate IS NULL OR (:date > t.orderDate AND :date < t.dispatchDate))")
    List<SaleTransaction> findByDate(@Param("date") LocalDate date);
}
