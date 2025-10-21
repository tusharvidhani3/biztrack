package com.tushar.biztrack.features.bill;

import java.util.List;

import com.tushar.biztrack.features.party.PartyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillResponse {
    private Long id;
    private PartyDto party;
    private BillType type;
    private Long additionalCharges;
    private List<BillItemResponse> items;
}
