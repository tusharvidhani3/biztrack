package com.tushar.biztrack.features.account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<AccountSummary> getAccounts(Pageable pageable, String partyName, PartyType partyType) {
        Page<Account> accounts = accountRepo.findByParty_NameAndParty_Type(pageable, partyName, partyType);
        Page<AccountSummary> accountSummaries = accounts.map(account -> {
            AccountSummary accountSummary = accountMapper.toSummary(account);

            Page<PaymentAccountEntry> paymentAccountEntries = paymentAccountEntryRepo.findLastClosingPayment(account.getId(), PageRequest.of(0, 1));
            PaymentAccountEntry lastClosingPayment = paymentAccountEntries.hasContent() ? paymentAccountEntries.getContent().get(0) : null;
            List<BillAccountEntry> billEntries;
            List<PaymentAccountEntry> paymentEntries;
            if(lastClosingPayment != null) {
                billEntries = billAccountEntryRepo.findBillsAfter(lastClosingPayment.getClosingBillAccountEntry().getId(), account.getId(), lastClosingPayment.getDate());
                paymentEntries = paymentAccountEntryRepo.findPaymentsAfter(lastClosingPayment.getId(), account.getId(), lastClosingPayment.getDate());
                accountSummary.setLastPaymentDate(lastClosingPayment.getDate());
            }
            else {
                billEntries = billAccountEntryRepo.findAll();
                paymentEntries = paymentAccountEntryRepo.findAll();
            }
            
            long totalPaidSinceLastClosing = 0l, totalBilledSinceLastClosing = 0l;
            for (PaymentAccountEntry entry : paymentEntries) {
                totalPaidSinceLastClosing += entry.getAmount();
            }
            for (BillAccountEntry entry : billEntries) {
                totalBilledSinceLastClosing += billService.getBillTotal(entry.getBill());
            }
            accountSummary.setOutstandingBalance(totalBilledSinceLastClosing - totalPaidSinceLastClosing);

            for (BillAccountEntry entry : billEntries) {
                if (totalPaidSinceLastClosing < billService.getBillTotal(entry.getBill())) {
                    accountSummary.setNextClosingDue(totalPaidSinceLastClosing);
                    break;
                }
                totalPaidSinceLastClosing -= billService.getBillTotal(entry.getBill());
            }
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
            billEntryResponse.setType(AccountEntryType.BILL);
            return billEntryResponse;
        }).collect(Collectors.toList());
        List<AccountEntryResponse> paymentEntries = paymentAccountEntries.stream().map(paymentAccountEntry -> {
            AccountEntryResponse paymentEntry = accountMapper.toResponse(paymentAccountEntry);
            paymentEntry.setType(AccountEntryType.PAYMENT);
            return paymentEntry;
        }).collect(Collectors.toList());
        Account account = accountRepo.findById(accountId).get();
        AccountResponse accountResponse = accountMapper.toResponse(account);

        Long amountPaidAfterLastClosing = 0L;
        for (AccountEntryResponse entry : paymentEntries) {
            amountPaidAfterLastClosing += entry.getAmount();
        }
        for (AccountEntryResponse entry : billEntries) {
            if (amountPaidAfterLastClosing < entry.getAmount()) {
                accountResponse.setNextClosingBillEntryId(entry.getId());
                accountResponse.setNextClosingDue(amountPaidAfterLastClosing);
                break;
            }
            amountPaidAfterLastClosing -= entry.getAmount();
        }

        int i = 0, j = 0;
        List<AccountEntryResponse> accountEntries = new ArrayList<>();
        while (i<billAccountEntries.size() && j<paymentAccountEntries.size()) {
            AccountEntryResponse billEntry = billEntries.get(i);
            AccountEntryResponse paymentEntry = paymentEntries.get(j);
            if(billEntry.getDate().isBefore(paymentEntry.getDate())) {
               accountEntries.add(billEntry);
               i++;
            }
            else {
                accountEntries.add(paymentEntry);
                j++;
            }
        }

        while(i<billEntries.size()) {
            accountEntries.add(billEntries.get(i));
            i++;
        }

        while(j<paymentEntries.size()) {
            accountEntries.add(paymentEntries.get(j));
            j++;
        }

        accountResponse.setEntries(accountEntries);

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
