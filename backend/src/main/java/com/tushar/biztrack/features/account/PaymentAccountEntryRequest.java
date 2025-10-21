package com.tushar.biztrack.features.account;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentAccountEntryRequest {
    
    private Long id;
    private Long accountId;
    private Long amount;
    private LocalDate date;
    private Long closingBillAccountEntryId;
    
}
