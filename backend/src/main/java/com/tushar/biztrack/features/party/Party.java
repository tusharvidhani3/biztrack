package com.tushar.biztrack.features.party;

import com.tushar.biztrack.features.business.BaseBusinessEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Party extends BaseBusinessEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    private String contactNumber;

    @Enumerated(EnumType.STRING)
    private PartyType type;

    private String city; // further make it Address

    private boolean active;

    // private boolean isDefaulted;
}
