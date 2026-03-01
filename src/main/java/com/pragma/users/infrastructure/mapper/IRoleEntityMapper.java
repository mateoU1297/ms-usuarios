package com.pragma.users.infrastructure.mapper;

import com.pragma.users.domain.model.Role;
import com.pragma.users.infrastructure.out.jpa.entity.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleEntityMapper {

    Role toRole(RolEntity entity);

    @Mapping(target = "userRoles", ignore = true)
    RolEntity toEntity(Role role);
}