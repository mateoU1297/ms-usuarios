package com.pragma.users.application.mapper;

import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.domain.model.AuthResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IJwtResponseMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "type", constant = "Bearer")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "roles", source = "authResult", qualifiedByName = "mapRoles")
    JwtResponse toJwtResponse(AuthResult authResult);

    @Named("mapRoles")
    default List<String> mapRoles(AuthResult authResult) {
        if (authResult.getUser() == null || authResult.getUser().getRoles() == null) {
            return new ArrayList<>();
        }
        return authResult.getUser().getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
}