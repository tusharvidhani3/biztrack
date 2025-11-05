package com.tushar.biztrack.features.AppUser;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserDto {

    private Long id;

    private String email;

    private Set<String> roles;
}
