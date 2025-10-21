package com.tushar.biztrack.features.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tushar.biztrack.features.bill.BillService;
import com.tushar.biztrack.features.party.PartyType;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRespository accountRepo;

    @Autowired
    private BillAccountEntryRepository billAccountEntryRepo;

    @Autowired
    private PaymentAccountEntryRepository paymentAccountEntryRepo;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private BillService billService;

    @Override
    public Page<AccountSummary> getAccounts(String partyName, PartyType partyType) {
        Page<Account> accounts = accountRepo.findByParty_NameAndParty_Type(partyName, partyType);
        Page<AccountSummary> accountSummaries = accounts.map(account -> {
            AccountSummary accountSummary = accountMapper.toSummary(account);
            return accountSummary;
        });
        return accountSummaries;
    }

    @Override
    public AccountResponse getAccount(Long accountId, Pageable pageable) {
        PaymentAccountEntry lastClosingPayment = paymentAccountEntryRepo.findLastClosingPayment(accountId, pageable).getContent().get(0);
        List<BillAccountEntry> billAccountEntries = billAccountEntryRepo.findBillsAfter(lastClosingPayment.getClosingBillAccountEntry().getId(), accountId, billService.getBillDate(lastClosingPayment.getClosingBillAccountEntry().getBill()));
        List<PaymentAccountEntry> paymentAccountEntries = paymentAccountEntryRepo.findPaymentsAfter(lastClosingPayment.getId(), accountId, lastClosingPayment.getDate());
        List<AccountEntryResponse> billEntries = billAccountEntries.stream().map(billAccountEntry -> {
            AccountEntryResponse billEntryResponse = accountMapper.toResponse(billAccountEntry);
            billEntryResponse.setDate(billService.getBillDate(billAccountEntry.getBill()));
            return billEntryResponse;
        }).collect(Collectors.toList());
        List<AccountEntryResponse> paymentEntries = paymentAccountEntries.stream().map(paymentAccountEntry -> accountMapper.toResponse(paymentAccountEntry)).collect(Collectors.toList());
        Account account = accountRepo.findById(accountId).get();
        AccountResponse accountResponse = accountMapper.toResponse(account);
        accountResponse.setBillEntries(billEntries);
        accountResponse.setPaymentEntries(paymentEntries);

        Long amountPaidAfterLastClosing = 0L;
        for (AccountEntryResponse entry : paymentEntries) {
            amountPaidAfterLastClosing += entry.getAmount();
        }
        for (AccountEntryResponse entry : billEntries) {
            if (amountPaidAfterLastClosing < entry.getAmount()) {
                accountResponse.setNextClosingBillEntryId(entry.getId());
                accountResponse.setNextClosingAmount(amountPaidAfterLastClosing);
                break;
            }
            amountPaidAfterLastClosing -= entry.getAmount();
        }

        int i = 0, j = 0;
        List<AccountEntryResponse> accountEntries = new ArrayList<>();
        while (i<billAccountEntries.size() && j<paymentAccountEntries.size()) {
            
        }

        return accountResponse;
    }

    @Override
    public AccountEntryResponse createPaymentAccountEntry(PaymentAccountEntryRequest paymentAccountEntryRequest) {
        PaymentAccountEntry paymentAccountEntry = accountMapper.toEntity(paymentAccountEntryRequest);
        Account account = accountRepo.findById(paymentAccountEntryRequest.getAccountId()).get();
        paymentAccountEntry.setAccount(account);
        PaymentAccountEntry lastClosingPayment = paymentAccountEntryRepo.findLastClosingPaymentAccountEntry(paymentAccountEntryRequest.getAccountId());
        List<BillAccountEntry> billEntries = billAccountEntryRepo.findBillsAfter(lastClosingPayment.getId(), paymentAccountEntryRequest.getAccountId(), lastClosingPayment.getDate());
        List<PaymentAccountEntry> paymentEntries = paymentAccountEntryRepo.findPaymentsAfter(lastClosingPayment.getId(), paymentAccountEntryRequest.getAccountId(), lastClosingPayment.getDate());

        if(paymentAccountEntryRequest.getClosingBillAccountEntryId() != null) {
            paymentAccountEntry.setClosingBillAccountEntry(billAccountEntryRepo.findById(paymentAccountEntryRequest.getClosingBillAccountEntryId()).get());
        }
        else {
            Long amountPaidAfterLastClosing = 0L;
            for (PaymentAccountEntry entry : paymentEntries)
                amountPaidAfterLastClosing += entry.getAmount();
                
            for (BillAccountEntry entry : billEntries) {
                long billAmount = billService.getBillTotal(entry.getBill());
                if (amountPaidAfterLastClosing < billAmount) {
                    if (amountPaidAfterLastClosing == paymentAccountEntry.getAmount())
                        paymentAccountEntry.setClosingBillAccountEntry(entry);
                break;
                }
            amountPaidAfterLastClosing -= billAmount;
            }
        }
        paymentAccountEntry = paymentAccountEntryRepo.save(paymentAccountEntry);
        return accountMapper.toResponse(paymentAccountEntry);
    }

}
