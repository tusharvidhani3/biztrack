package com.tushar.biztrack.features.bill;

import com.tushar.biztrack.features.business.BaseBusinessEntity;
import com.tushar.biztrack.features.party.Party;
import com.tushar.biztrack.features.transaction.SaleTransaction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bill extends BaseBusinessEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private Party party;

    @Enumerated(EnumType.STRING)
    private BillType type;

    @OneToOne
    private SaleTransaction saleTransaction;

    private Long additionalCharges;
}
