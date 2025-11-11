package com.tushar.biztrack.features.bill;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BillService {
    BillResponse getBill(Long billId);
    Page<BillResponse> getBills(Pageable pageable, String partyName);
    BillResponse createBill(BillRequest billRequest);
    BillResponse updateBill(BillRequest billRequest);
    void deleteBill(Long billId);
    long getBillTotal(Bill bill);
    LocalDate getBillDate(Bill bill);
    Bill getBillById(Long billId);
}
