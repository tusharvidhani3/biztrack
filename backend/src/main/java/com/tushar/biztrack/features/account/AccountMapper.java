package com.tushar.biztrack.features.account;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
    AccountSummary toSummary(Account account);
    
    AccountEntryResponse toResponse(BillAccountEntry billAccountEntry);
    AccountEntryResponse toResponse(PaymentAccountEntry paymentAccountEntry);

    PaymentAccountEntry toEntity(PaymentAccountEntryRequest paymentAccountEntryRequest);
}
