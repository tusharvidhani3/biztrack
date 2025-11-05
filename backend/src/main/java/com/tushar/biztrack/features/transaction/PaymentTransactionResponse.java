package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;

import com.tushar.biztrack.features.party.PartyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentTransactionResponse {
    
    private Long id;
    private PartyDto party;
    private PaymentDirection direction;
    private Long amount;
    private LocalDate paymentDate;
}
