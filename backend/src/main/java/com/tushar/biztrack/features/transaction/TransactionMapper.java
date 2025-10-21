package com.tushar.biztrack.features.transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "orderDate", target = "initiationDate")
    @Mapping(source = "dispatchDate", target = "completionDate")
    GoodsTransactionResponse toResponse(SaleTransaction saleTransaction);

    @Mapping(source = "orderDate", target = "initiationDate")
    @Mapping(source = "dispatchDate", target = "completionDate")
    GoodsTransactionResponse toResponse(PurchaseTransaction purchaseTransaction);

    @Mapping(source = "initiationDate", target = "orderDate")
    @Mapping(source = "completionDate", target = "dispatchDate")
    SaleTransaction toSaleTransactionEntity(GoodsTransactionRequest goodsTransactionRequest);

    @Mapping(source = "initiationDate", target = "orderDate")
    @Mapping(source = "completionDate", target = "dispatchDate")
    PurchaseTransaction toPurchseTransactionEntity(GoodsTransactionRequest goodsTransactionRequest);

    PaymentTransaction toEntity(PaymentTransactionRequest paymentTransactionRequest);
    PaymentTransactionResponse toResponse(PaymentTransaction paymentTransaction);

    SaleEntry toSaleEntryEntity(GoodsEntryRequest entryRequest);
    PurchaseEntry toPurchaseEntryEntity(GoodsEntryRequest entryRequest);

    GoodsEntryResponse toResponse(SaleEntry saleEntry);
    GoodsEntryResponse toResponse(PurchaseEntry purchaseEntry);
}
