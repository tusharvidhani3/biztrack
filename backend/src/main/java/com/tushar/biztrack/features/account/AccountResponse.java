package com.tushar.biztrack.features.account;

import java.util.List;

import com.tushar.biztrack.features.party.PartyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountResponse {
    private Long id;
    private PartyDto party;
    private List<AccountEntryResponse> entries;
    private Long nextClosingDue;
    private Long nextClosingBillEntryId;
    private long totalDue;
}
