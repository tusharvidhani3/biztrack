package com.tushar.biztrack.features.bill;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillItemRequest {
    private Long id;
    private Long productId;
    private Long price;
    private Long quantity;
}
