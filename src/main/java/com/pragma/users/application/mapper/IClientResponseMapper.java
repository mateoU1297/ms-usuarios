package com.pragma.users.application.mapper;

import com.pragma.users.application.dto.ClientRequest;
import com.pragma.users.application.dto.ClientResponse;
import com.pragma.users.domain.model.User;
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
public interface IClientResponseMapper {

    User toModel(ClientRequest clientRequest);

    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoles")
    ClientResponse toResponse(User user);

    @Named("mapRoles")
    default List<String> mapRoles(User user) {
        if (user == null || user.getRoles() == null) {
            return new ArrayList<>();
        }
        return user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());
    }
}