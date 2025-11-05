package com.tushar.biztrack.features.AppUser;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    
    AppUserDto toDto(AppUser appUser);

    default Set<String> map(Set<Role> roles) {
        return roles.stream().map(role -> role.getName()).collect(Collectors.toSet());
    }
}
