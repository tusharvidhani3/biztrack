package com.tushar.biztrack.features.party;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PartyMapper {
    Party toEntity(PartyDto partyDto);
    PartyDto toDto(Party party);
    PartySnapshot toSnapshot(Party party);
    PartySnapshot toSnapshot(PartyDto partyDto);
}