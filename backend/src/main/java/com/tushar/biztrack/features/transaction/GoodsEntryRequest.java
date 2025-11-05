package com.tushar.biztrack.features.transaction;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsEntryRequest {
    private Long id;
    private Long productId;
    private Integer numberOfBags;
    private Long price;
    private List<Long> quantities;
}
