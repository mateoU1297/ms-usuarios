package com.pragma.usuarios.application.mapper;

import com.pragma.usuarios.application.dto.OwnerRequest;
import com.pragma.usuarios.application.dto.OwnerResponse;
import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface IOwnerResponseMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToNames")
    OwnerResponse toResponse(User user);

    User toModel(OwnerRequest  ownerRequest);

    @Named("mapRolesToNames")
    default List<String> mapRolesToNames(Set<Role> roles) {

        if (roles == null) {
            return List.of();
        }

        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }

}