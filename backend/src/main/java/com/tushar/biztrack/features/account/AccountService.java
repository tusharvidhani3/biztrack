package com.tushar.biztrack.features.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tushar.biztrack.features.party.PartyType;

public interface AccountService {
    Page<AccountSummary> getAccounts(Pageable pageable, String partyName, PartyType partyType);
    AccountResponse getAccount(Long accountId, Pageable pageable);
    AccountEntryResponse createPaymentAccountEntry(PaymentAccountEntryRequest paymentAccountEntryRequest);

}
