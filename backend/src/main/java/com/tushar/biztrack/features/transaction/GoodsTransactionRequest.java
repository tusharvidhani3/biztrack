package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsTransactionRequest {

    private Long id;

    private Long partyId;

    private LocalDate initiationDate;

    private LocalDate completionDate;

    private PaymentMethod paymentMethod;

    private List<GoodsEntryRequest> entries;

    private String note;
}
