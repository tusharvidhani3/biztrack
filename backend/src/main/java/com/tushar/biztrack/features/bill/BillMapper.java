package com.tushar.biztrack.features.bill;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillMapper {
    BillResponse toResponse(Bill bill);
    Bill toEntity(BillRequest billRequest);
}
