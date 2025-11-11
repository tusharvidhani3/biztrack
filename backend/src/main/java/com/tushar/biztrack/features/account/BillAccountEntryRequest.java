package com.tushar.biztrack.features.account;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillAccountEntryRequest {
    private Long billId;
    private Long partyId;
}
