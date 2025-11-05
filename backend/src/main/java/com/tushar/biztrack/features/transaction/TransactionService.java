package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;

public interface TransactionService {
    
    // Long calculateQuantityLost(Long purchaseEntryId);
    DailyTransactions getTransactionsByDate(LocalDate date);
    PaymentTransactionResponse createPaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
    void deletePaymentTransaction(Long paymentTransactionId);
    PaymentTransactionResponse updatePaymentTransaction(PaymentTransactionRequest paymentTransactionRequest);
    GoodsTransactionResponse createSaleTransaction(GoodsTransactionRequest goodsTransactionRequest);
    GoodsTransactionResponse createPurchaseTransaction(GoodsTransactionRequest goodsTransactionRequest);
    void deleteSaleTransaction(Long saleTransactionId);
    void deletePurchaseTransaction(Long purchaseTransactionId);
    GoodsTransactionResponse updateSaleTransaction(GoodsTransactionRequest goodsTransactionRequest);
    GoodsTransactionResponse updatePurchaseTransaction(GoodsTransactionRequest goodsTransactionRequest);
    long getSaleTransactionAmount(Long saleTransactionId);
    long calculateProfit(Long saleTransactionId);
}
