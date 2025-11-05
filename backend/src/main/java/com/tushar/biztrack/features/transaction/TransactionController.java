package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<DailyTransactions> getTransactionsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyTransactions dailyTransactions = transactionService.getTransactionsByDate(date);
        return ResponseEntity.ok(dailyTransactions);
    }

    @PutMapping("/sales/{saleTransactionId}")
    public ResponseEntity<GoodsTransactionResponse> updateSaleTransaction(@RequestBody GoodsTransactionRequest goodsTransactionRequest) {
        GoodsTransactionResponse goodsTransactionResponse = transactionService.updateSaleTransaction(goodsTransactionRequest);
        return ResponseEntity.ok(goodsTransactionResponse);
    }

    @PutMapping("/purchases/{purchaseTransactionId}")
    public ResponseEntity<GoodsTransactionResponse> updatePurchaseTransaction(@RequestBody GoodsTransactionRequest goodsTransactionRequest) {
        GoodsTransactionResponse goodsTransactionResponse = transactionService.updatePurchaseTransaction(goodsTransactionRequest);
        return ResponseEntity.ok(goodsTransactionResponse);
    }

    // @PutMapping('/{saleTransactionId}')
    // public ResponseEntity<Void> markDispatched(@RequestBody ) {

    // }
}
