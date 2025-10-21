package com.tushar.biztrack.features.bill;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BillItemMapper {
    BillItem toEntity(BillItemRequest billItemRequest);

    @Mapping(source = "product.name", target = "productName")
    BillItemResponse toResponse(BillItem billItem);
}
