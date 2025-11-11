package com.tushar.biztrack.features.bill;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillRequest {
    
    private Long id;
    private Long partyId;
    private BillType type;
    private Long additionalCharges;
    private List<BillItemRequest> items;
}
