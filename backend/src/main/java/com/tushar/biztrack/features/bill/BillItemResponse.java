package com.tushar.biztrack.features.bill;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BillItemResponse {
    private Long id;
    private String productName;
    private Long price;
    private Long quantity;
}
