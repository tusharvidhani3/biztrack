package com.tushar.biztrack.features.party;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PartyDto {
    private Long id;
    private String name;
    private String contactNumber;
    private PartyType type;
    private String city;
}
