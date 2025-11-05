package com.tushar.biztrack.features.transaction;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsEntryResponse {
    private Long id;
    private String productName;
    private Integer numberOfBags;
    private Long price;
    private List<Long> quantities;
}
