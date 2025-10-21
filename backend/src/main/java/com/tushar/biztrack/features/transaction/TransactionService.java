package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;

public interface TransactionService {
    
    Long calculateQuantityLost(Long purchaseEntryId);
    DailyTransactions getTransactionsByDate(LocalDate date);
    PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
    void deletePaymentTransaction(Long paymentTransactionId);
    PaymentTransactionResponse updatePaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
    GoodsTransactionResponse createSaleTransaction(GoodsTransactionRequest goodsTransactionRequest);
    void deleteGoodsTransaction(Long goodsTransactionId);
    GoodsTransactionResponse updateGoodsTransaction(GoodsTransactionRequest goodsTransactionRequest);
    long getSaleTransactionAmount(Long saleTransactionId);
    long calculateProfit(Long saleTransactionId);
}
