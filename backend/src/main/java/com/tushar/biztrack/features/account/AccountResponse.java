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
    private List<AccountEntryResponse> billEntries;
    private List<AccountEntryResponse> paymentEntries;
    private Long nextClosingAmount;
    private Long nextClosingBillEntryId;
    private long totalDue;
}
