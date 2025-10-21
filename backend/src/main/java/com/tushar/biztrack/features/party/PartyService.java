package com.tushar.biztrack.features.party;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartyService {
    public PartyDto getParty(Long partyId);
    public Page<PartyDto> getParties(String name, PartyType type, Pageable pageable);
    public PartyDto createParty(PartyDto partyDto);
    public PartyDto updateParty(PartyDto partyDto);
    public Party getPartyById(Long partyId);
}
