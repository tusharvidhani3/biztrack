package com.tushar.biztrack.features.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleEntryRepository extends JpaRepository<SaleEntry,Long> {
    List<SaleEntry> findByPurchaseEntry_Id(Long purchaseEntryId);

    List<SaleEntry> findBySaleTransaction_Id(Long saleTransactionId);
}
