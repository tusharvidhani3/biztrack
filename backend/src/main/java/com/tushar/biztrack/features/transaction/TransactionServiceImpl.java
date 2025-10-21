package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tushar.biztrack.features.party.Party;
import com.tushar.biztrack.features.party.PartyMapper;
import com.tushar.biztrack.features.party.PartyService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private SaleTransactionRepository saleTransactionRepo;

    @Autowired
    private PurchaseTransactionRepository purchaseTransactionRepo;

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepo;

    @Autowired
    private SaleEntryRepository saleEntryRepo;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyMapper partyMapper;
    
    @Override
    public Long calculateQuantityLost(Long purchaseEntryId) {
        List<SaleEntry> saleEntries = saleEntryRepo.findAllByPurchaseEntry_Id(purchaseEntryId);
        Long quantitySold = saleEntries.stream().reduce(0, (qty, saleEntry) -> qty + saleEntry.getQuantity(), Long::sum);
        return quantitySold;
    }

    @Override
    public DailyTransactions getTransactionsByDate(LocalDate date) {
        List<GoodsTransactionResponse> sales = saleTransactionRepo.findByDateWithEntries(date).stream().map(saleTransaction -> {
            GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(saleTransaction);
            goodsTransactionResponse.setParty(partyMapper.toDto(saleTransaction.getParty()));
            return goodsTransactionResponse;
        }).collect(Collectors.toList());

        List<GoodsTransactionResponse> purchases = purchaseTransactionRepo.findByDateWithEntries(date).stream().map(purchaseTransaction -> {
            GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(purchaseTransaction);
            goodsTransactionResponse.setParty(partyMapper.toDto(purchaseTransaction.getParty()));
            return goodsTransactionResponse;
        }).collect(Collectors.toList());

        List<PaymentTransactionResponse> sentPayments = paymentTransactionRepo.findByPaymentDateAndDirection(date, PaymentDirection.SENT).stream().map(paymentTransaction -> {
            PaymentTransactionResponse paymentTransactionResponse = transactionMapper.toResponse(paymentTransaction);
            paymentTransactionResponse.setParty(partyMapper.toDto(paymentTransaction.getParty()));
            return paymentTransactionResponse;
        }).collect(Collectors.toList());

        List<PaymentTransactionResponse> receivedPayments = paymentTransactionRepo.findByPaymentDateAndDirection(date, PaymentDirection.RECEIVED).stream().map(paymentTransaction -> {
            PaymentTransactionResponse paymentTransactionResponse = transactionMapper.toResponse(paymentTransaction);
            paymentTransactionResponse.setParty(partyMapper.toDto(paymentTransaction.getParty()));
            return paymentTransactionResponse;
        }).collect(Collectors.toList());

        DailyTransactions dailyTransactions = new DailyTransactions();
        dailyTransactions.setDate(date);
        dailyTransactions.setSaleTransactions(sales);
        dailyTransactions.setSentPaymentTransactions(sentPayments);
        dailyTransactions.setPurchaseTransactions(purchases);
        dailyTransactions.setReceivedPaymentTransactions(receivedPayments);

        return dailyTransactions;
    }

    @Override
    public GoodsTransactionResponse createSaleTransaction(GoodsTransactionRequest goodsTransactionRequest) {
        SaleTransaction saleTransaction = transactionMapper.toSaleTransactionEntity(goodsTransactionRequest);
        Party party = partyService.getPartyById(goodsTransactionRequest.getPartyId());
        saleTransaction.setParty(party);
        if(goodsTransactionRequest.getInitiationDate() == null)
            saleTransaction.setOrderDate(LocalDate.now());
        List<SaleEntry> entries = goodsTransactionRequest.getEntries().stream().map(entryDto -> {
            SaleEntry saleEntry = transactionMapper.toSaleEntryEntity(entryDto);
            saleEntry.setSaleTransaction(saleTransaction);
            // if stocks from more than 1 purchase entries are present for the ordered goods, optionally ask the user to select purchase entry(s), else select the 1 that is present
            saleEntry = saleEntryRepo.save(saleEntry);
            return saleEntry;
        }).collect(Collectors.toList());
        
        GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(saleTransaction);
        return goodsTransactionResponse;
    }

    @Override
    public long getSaleTransactionAmount(Long saleTransactionId) {
        long saleValue = 0L;
        List<SaleEntry> sales = saleEntryRepo.findBySaleTransaction_Id(saleTransactionId);
        for(SaleEntry sale : sales) {
            saleValue += sale.getPrice() * sale.getQuantity();
        }
        return saleValue;
    }

    @Override
    public long calculateProfit(Long saleTransactionId) {
        List<SaleEntry> sales = saleEntryRepo.findBySaleTransaction_Id(saleTransactionId);
        long profit = 0L;
        for(SaleEntry saleEntry : sales) {
            long costPrice = saleEntry.getPurchaseEntries().stream().mapToLong(purchaseEntry -> purchaseEntry.getPrice()).sum();
        }
        return profit;
    }
    
}
