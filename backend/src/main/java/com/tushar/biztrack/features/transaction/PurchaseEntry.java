package com.tushar.biztrack.features.transaction;

import com.tushar.biztrack.features.product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PurchaseEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private PurchaseTransaction purchaseTransaction;

    @ManyToOne
    private Product product;

    private Integer numberOfBags;

    private Long price;

    private Long quantity;
}
