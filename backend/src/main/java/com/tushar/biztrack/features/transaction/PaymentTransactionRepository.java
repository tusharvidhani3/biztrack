package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    
    @Query("SELECT t FROM PaymentTransaction t WHERE t.paymentDate = :date AND t.direction = :direction")
    List<PaymentTransaction> findByPaymentDateAndDirection(LocalDate date, PaymentDirection direction);
}
