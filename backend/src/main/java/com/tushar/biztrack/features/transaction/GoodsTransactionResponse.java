package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import com.tushar.biztrack.features.party.PartyDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsTransactionResponse {

    private Long id;

    private PartyDto party; // name

    private LocalDate initiationDate;

    private LocalDate completionDate;

    private PaymentMethod paymentMethod;

    private List<GoodsEntryResponse> entries;

    private String note;
}
