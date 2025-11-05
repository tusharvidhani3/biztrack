package com.tushar.biztrack.features.product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private Boolean active;
    private long lastSellPrice;
    private long lastPurchasePrice;
}
