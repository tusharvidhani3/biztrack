package com.tushar.biztrack.features.account;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tushar.biztrack.features.party.PartyType;

@Repository
public interface AccountRespository extends JpaRepository<Account,Long> {

    Page<Account> findByParty_NameAndParty_Type(String partyName, PartyType partyType);
}
