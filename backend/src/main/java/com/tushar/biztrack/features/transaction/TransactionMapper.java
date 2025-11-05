package com.tushar.biztrack.features.transaction;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {
    @Mapping(source = "orderDate", target = "initiationDate")
    @Mapping(source = "dispatchDate", target = "completionDate")
    GoodsTransactionResponse toResponse(SaleTransaction saleTransaction);

    @Mapping(source = "orderDate", target = "initiationDate")
    @Mapping(source = "receivedDate", target = "completionDate")
    GoodsTransactionResponse toResponse(PurchaseTransaction purchaseTransaction);

    @Mapping(source = "initiationDate", target = "orderDate")
    @Mapping(source = "completionDate", target = "dispatchDate")
    SaleTransaction toSaleTransactionEntity(GoodsTransactionRequest goodsTransactionRequest);

    @Mapping(source = "initiationDate", target = "orderDate")
    @Mapping(source = "completionDate", target = "receivedDate")
    PurchaseTransaction toPurchseTransactionEntity(GoodsTransactionRequest goodsTransactionRequest);

    PaymentTransaction toEntity(PaymentTransactionRequest paymentTransactionRequest);
    PaymentTransactionResponse toResponse(PaymentTransaction paymentTransaction);

    SaleEntry toSaleEntryEntity(GoodsEntryRequest entryRequest);
    PurchaseEntry toPurchaseEntryEntity(GoodsEntryRequest entryRequest);

    GoodsEntryResponse toResponse(SaleEntry saleEntry);

    @Mapping(source = "product.name", target = "productName")
    GoodsEntryResponse toResponse(PurchaseEntry purchaseEntry);

    void updateSaleTransaction(GoodsTransactionRequest goodsTransactionRequest, @MappingTarget SaleTransaction saleTransaction);
    void updatePaymentTransaction(PaymentTransactionRequest paymentTransactionRequest, @MappingTarget PaymentTransaction paymentTransaction);

    void updateSaleEntry(GoodsEntryRequest goodsEntryRequest, @MappingTarget SaleEntry saleEntry);
    void updatePurchaseEntry(GoodsEntryRequest goodsEntryRequest, @MappingTarget PurchaseEntry purchaseEntry);
}
