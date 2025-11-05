package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentTransactionRequest {

    private Long id;
    private Long partyId;
    private Long amount;
    private PaymentDirection direction;
    private LocalDate paymentDate;
}
