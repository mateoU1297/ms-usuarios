package com.pragma.users.application.mapper;

import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IJwtResponseMapper {

    @Mapping(target = "token", ignore = true)
    @Mapping(target = "type", constant = "Bearer")
    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoles")
    JwtResponse toJwtResponse(User user);

    @Named("mapRoles")
    default java.util.List<String> mapRoles(User user) {
        if (user == null || user.getRoles() == null) {
            return new java.util.ArrayList<>();
        }
        return user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(java.util.stream.Collectors.toList());
    }
}