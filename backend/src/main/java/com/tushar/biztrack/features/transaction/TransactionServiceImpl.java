package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tushar.biztrack.common.exception.ResourceNotFoundException;
import com.tushar.biztrack.features.party.Party;
import com.tushar.biztrack.features.party.PartyDto;
import com.tushar.biztrack.features.party.PartyMapper;
import com.tushar.biztrack.features.party.PartyService;
import com.tushar.biztrack.features.product.Product;
import com.tushar.biztrack.features.product.ProductService;

import jakarta.transaction.Transactional;

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
    private PurchaseEntryRepository purchaseEntryRepo;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private PartyService partyService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PartyMapper partyMapper;
    
    // @Override
    // public Long calculateQuantityLost(Long purchaseEntryId) {
    //     List<SaleEntry> saleEntries = saleEntryRepo.findAllByPurchaseEntry_Id(purchaseEntryId);
    //     Long quantitySold = saleEntries.stream().reduce(0, (qty, saleEntry) -> qty + saleEntry.getQuantity(), Long::sum);
    //     return quantitySold;
    // }

    @Override
    public DailyTransactions getTransactionsByDate(LocalDate date) {
        List<GoodsTransactionResponse> sales = saleTransactionRepo.findByDate(date).stream().map(saleTransaction -> {
            GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(saleTransaction);
            goodsTransactionResponse.setParty(partyMapper.toDto(saleTransaction.getParty()));
            return goodsTransactionResponse;
        }).collect(Collectors.toList());

        List<GoodsTransactionResponse> purchases = purchaseTransactionRepo.findByDate(date).stream().map(purchaseTransaction -> {
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

    @Override
    public PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransaction paymentTransaction = transactionMapper.toEntity(paymentTransactionRequest);
        Party party = partyService.getPartyById(paymentTransactionRequest.getPartyId());
        paymentTransaction.setParty(party);
        return transactionMapper.toResponse(paymentTransaction);
    }

    @Override
    public void deletePaymentTransaction(Long paymentTransactionId) {
        paymentTransactionRepo.deleteById(paymentTransactionId);
    }

    @Override
    public PaymentTransactionResponse updatePaymentTransaction(PaymentTransactionRequest paymentTransactionRequest) {
        PaymentTransaction paymentTransaction = paymentTransactionRepo.findById(paymentTransactionRequest.getId()).get();
        transactionMapper.updatePaymentTransaction(paymentTransactionRequest, paymentTransaction);
        Party party = partyService.getPartyById(paymentTransactionRequest.getPartyId());
        paymentTransaction.setParty(party);
        paymentTransaction = paymentTransactionRepo.save(paymentTransaction);
        PaymentTransactionResponse paymentTransactionResponse = transactionMapper.toResponse(paymentTransaction);
        return paymentTransactionResponse;
    }

    @Override
    @Transactional
    public void deleteSaleTransaction(Long saleTransactionId) {
        saleTransactionRepo.deleteById(saleTransactionId);
        saleEntryRepo.deleteBySaleTransaction_Id(saleTransactionId);
    }

    @Override
    @Transactional
    public void deletePurchaseTransaction(Long purchaseTransactionId) {
        purchaseTransactionRepo.deleteById(purchaseTransactionId);
        purchaseEntryRepo.deleteByPurchaseTransaction_Id(purchaseTransactionId);
    }

    @Override
    @Transactional
    public GoodsTransactionResponse updateSaleTransaction(GoodsTransactionRequest goodsTransactionRequest) {
        SaleTransaction saleTransaction = saleTransactionRepo.findById(goodsTransactionRequest.getId()).orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        transactionMapper.updateSaleTransaction(goodsTransactionRequest, saleTransaction);
        Party party = partyService.getPartyById(goodsTransactionRequest.getPartyId());
        saleTransaction.setParty(party);
        saleTransaction = saleTransactionRepo.save(saleTransaction);
        GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(saleTransaction);
        
        List<SaleEntry> saleEntries = new ArrayList<>();

        for(GoodsEntryRequest goodsEntryRequest : goodsTransactionRequest.getEntries()) {
            SaleEntry saleEntry;
            if(goodsEntryRequest.getId() != null) {
                saleEntry = saleEntryRepo.findById(goodsEntryRequest.getId()).get();
            }
            else
                saleEntry = new SaleEntry();
            transactionMapper.updateSaleEntry(goodsEntryRequest, saleEntry);
            saleEntries.add(saleEntry);
        }
        saleEntries = saleEntryRepo.saveAll(saleEntries);

        List<GoodsEntryResponse> goodsEntryResponses = saleEntries.stream()
        .map(transactionMapper::toResponse)
        .collect(Collectors.toList());

        goodsTransactionResponse.setEntries(goodsEntryResponses);
        return goodsTransactionResponse;
    }

    @Override
    @Transactional
    public GoodsTransactionResponse createPurchaseTransaction(GoodsTransactionRequest goodsTransactionRequest) {
        PurchaseTransaction purchaseTransaction = transactionMapper.toPurchseTransactionEntity(goodsTransactionRequest);
        Party party = partyService.getPartyById(goodsTransactionRequest.getPartyId());
        purchaseTransaction.setParty(party);
        purchaseTransaction = purchaseTransactionRepo.save(purchaseTransaction);
        List<PurchaseEntry> purchaseEntries = new ArrayList<>();

        for(GoodsEntryRequest goodsEntryRequest : goodsTransactionRequest.getEntries()) {
            PurchaseEntry purchaseEntry = transactionMapper.toPurchaseEntryEntity(goodsEntryRequest);
            purchaseEntry.setPurchaseTransaction(purchaseTransaction);
            purchaseEntry.setProduct(productService.getProductById(goodsEntryRequest.getProductId()));
            purchaseEntries.add(purchaseEntry);
        }

        purchaseEntries = purchaseEntryRepo.saveAll(purchaseEntries);
        GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(purchaseTransaction);
        List<GoodsEntryResponse> goodsEntryResponses = purchaseEntries.stream().map(purchaseEntry -> transactionMapper.toResponse(purchaseEntry)).collect(Collectors.toList());
        goodsTransactionResponse.setEntries(goodsEntryResponses);
        return goodsTransactionResponse;
    }

    @Override
    @Transactional
    public GoodsTransactionResponse updatePurchaseTransaction(GoodsTransactionRequest goodsTransactionRequest) {
        PurchaseTransaction purchaseTransaction = transactionMapper.toPurchseTransactionEntity(goodsTransactionRequest);
        Party party = partyService.getPartyById(goodsTransactionRequest.getPartyId());
        purchaseTransaction.setParty(party);
        purchaseTransaction = purchaseTransactionRepo.save(purchaseTransaction);
        GoodsTransactionResponse goodsTransactionResponse = transactionMapper.toResponse(purchaseTransaction);

        List<PurchaseEntry> purchaseEntries = new ArrayList<>();

        for(GoodsEntryRequest goodsEntryRequest : goodsTransactionRequest.getEntries()) {
            PurchaseEntry purchaseEntry;
            if(goodsEntryRequest.getId() != null)
                purchaseEntry = purchaseEntryRepo.findById(goodsEntryRequest.getId()).get();
            else
                purchaseEntry = new PurchaseEntry();
            transactionMapper.updatePurchaseEntry(goodsEntryRequest, purchaseEntry);
            purchaseEntry.setProduct(productService.getProductById(goodsEntryRequest.getProductId()));
            purchaseEntries.add(purchaseEntry);
        }
        purchaseEntryRepo.saveAll(purchaseEntries);
        List<GoodsEntryResponse> goodsEntryResponses = purchaseEntries.stream()
        .map(transactionMapper::toResponse)
        .collect(Collectors.toList());
        goodsTransactionResponse.setEntries(goodsEntryResponses);
        return goodsTransactionResponse;
    }

    
    
}
