package com.tushar.biztrack.features.account;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAccountEntryRepository extends JpaRepository<PaymentAccountEntry, Long> {

    @Query("""
            SELECT p.id
            FROM PaymentAccountEntry p
            WHERE p.account.id = :accountId AND p.closingBillAccountEntry IS NOT NULL
            ORDER BY p.date DESC
            """)
    Page<PaymentAccountEntry> findLastClosingPayment(@Param("accountId") Long accountId, Pageable pageable);

    @Query("""
            SELECT p.id
            FROM PaymentAccountEntry p
            WHERE p.account.id = :accountId AND p.closingBillAccountEntry IS NOT NULL
            ORDER BY p.date DESC
            """)
    PaymentAccountEntry findLastClosingPaymentAccountEntry(@Param("accountId") Long accountId);

    @Query("""
        SELECT p
        FROM PaymentAccountEntry p
        WHERE (p.date > :date
               OR (p.date = :date AND p.id > :paymentAccountEntryId))
          AND p.account.id = :accountId
        ORDER BY p.date ASC, p.id ASC
    """)
    List<PaymentAccountEntry> findPaymentsAfter(@Param("paymentAccountEntryId") Long lastClosingPaymentId, @Param("accountId") Long accountId, LocalDate date);
}
