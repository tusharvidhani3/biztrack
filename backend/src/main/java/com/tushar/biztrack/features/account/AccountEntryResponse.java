package com.tushar.biztrack.features.account;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountEntryResponse {
    private Long id;
    private Long amount;
    private LocalDate date;
}
