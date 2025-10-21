package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DailyTransactions {
    private LocalDate date;
    private List<GoodsTransactionResponse> saleTransactions;
    private List<GoodsTransactionResponse> purchaseTransactions;
    private List<PaymentTransactionResponse> sentPaymentTransactions;
    private List<PaymentTransactionResponse> receivedPaymentTransactions;
}
