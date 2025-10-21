package com.tushar.biztrack.features.account;

import com.tushar.biztrack.features.party.PartyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountSummary {
    private Long id;
    private PartyDto party;
}
