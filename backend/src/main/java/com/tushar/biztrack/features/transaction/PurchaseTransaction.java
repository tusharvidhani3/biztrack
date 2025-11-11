package com.tushar.biztrack.features.transaction;

import java.time.LocalDate;

import com.tushar.biztrack.features.party.Party;

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
public class PurchaseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private Party party;

    private LocalDate orderDate; // later make it localdatetime

    private LocalDate receivedDate;

    private PaymentSettlement paymentSettlement;

    private String note;
}
