package com.pragma.users.application.mapper;

import com.pragma.users.application.dto.EmployeeRequest;
import com.pragma.users.application.dto.EmployeeResponse;
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
public interface IEmployeeResponseMapper {
    User toModel(EmployeeRequest employeeRequest);

    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoles")
    EmployeeResponse toResponse(User user);

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
