package com.tushar.biztrack.features.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsEntryResponse {
    private Long id;
    private String productName;
    private Integer numberOfBags;
    private Long price, quantity;
}
