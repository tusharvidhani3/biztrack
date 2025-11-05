package com.tushar.biztrack.features.account;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillAccountEntryRepository extends JpaRepository<BillAccountEntry, Long> {

    @Query("""
        SELECT b
        FROM BillAccountEntry b
        JOIN b.bill bill
        JOIN b.bill.saleTransaction t
        WHERE (t.dispatchDate > :date
               OR (t.dispatchDate = :date AND b.id > :billAccountEntryId))
          AND b.account.id = :accountId
        ORDER BY t.dispatchDate ASC, b.id ASC
    """)
    List<BillAccountEntry> findBillsAfter(@Param("billAccountEntryId") Long lastClosingBillAccountEntryId, @Param("accountId") Long accountId, @Param("date") LocalDate date);

}
