package com.tushar.biztrack.features.bill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem,Long> {
    List<BillItem> findByBill_Id(Long billId);
    void deleteByBill_Id(Long billId);
}
