package com.tushar.biztrack.features.transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseEntryRepository extends JpaRepository<PurchaseEntry, Long> {
    List<PurchaseEntry> findByPurchaseTransaction_Id(Long saleTransactionId);
    void deleteByPurchaseTransaction_Id(Long purchaseTransactionId);
}
