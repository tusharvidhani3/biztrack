package com.tushar.biztrack.features.account;

import java.time.LocalDate;

import com.tushar.biztrack.features.party.PartyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountSummary {
    private Long id;
    private PartyDto party;
    private long outstandingBalance;
    private LocalDate lastPaymentDate;
    private long nextClosingDue;
}
