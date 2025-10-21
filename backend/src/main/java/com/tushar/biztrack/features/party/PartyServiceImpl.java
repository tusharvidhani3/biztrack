package com.tushar.biztrack.features.party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class PartyServiceImpl implements PartyService {

    @Autowired
    private PartyRepository partyRepo;

    @Autowired
    private PartyMapper partyMapper;

    @Override
    public PartyDto getParty(Long partyId) {
        Party party = partyRepo.findById(partyId).get();
        return partyMapper.toDto(party);
    }

    @Override
    public Page<PartyDto> getParties(String name, PartyType type, Pageable pageable) {
        Page<Party> parties = partyRepo.findByNameAndType(name, type, pageable);
        return parties.map(party -> partyMapper.toDto(party));
    }

    @Override
    public PartyDto createParty(PartyDto partyDto) {
        Party party = partyMapper.toEntity(partyDto);
        partyRepo.save(party);
        return partyMapper.toDto(party);
    }

    @Override
    @Transactional
    public PartyDto updateParty(PartyDto partyDto) {
        Party party = partyRepo.findById(partyDto.getId()).get();
        if(partyDto.getContactNumber() != null)
            party.setContactNumber(partyDto.getContactNumber());
        if(partyDto.getCity() != null)
            party.setCity(partyDto.getCity());
        if(partyDto.getName() != null)
            party.setName(partyDto.getName());
        if(partyDto.getType() != null)
            party.setType(partyDto.getType());
        return partyMapper.toDto(party);
    }

    @Override
    public Party getPartyById(Long partyId) {
        return partyRepo.findById(partyId).get();
    }
    
}
