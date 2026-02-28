package com.pragma.usuarios.infrastructure.mapper;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserRoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IRoleEntityMapper.class})
public interface IUserEntityMapper {

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "mapUserRolesToRoles")
    User toUser(UserEntity entity);

    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserEntity toEntity(User user);

    @Named("mapUserRolesToRoles")
    default Set<Role> mapUserRolesToRoles(Set<UserRoleEntity> userRoles) {
        if (userRoles == null) {
            return new HashSet<>();
        }
        return userRoles.stream()
                .map(userRoleEntity -> toRole(userRoleEntity.getRole()))
                .collect(Collectors.toSet());
    }

    Role toRole(RolEntity entity);
}