package com.pragma.usuarios.infrastructure.mapper;

import com.pragma.usuarios.domain.model.Role;
import com.pragma.usuarios.infrastructure.entity.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleEntityMapper {

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "createdAt", source = "createdAt")
//    @Mapping(target = "updatedAt", source = "updatedAt")
//    Role toRole(RolEntity entity);

//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "createdAt", source = "createdAt")
//    @Mapping(target = "updatedAt", source = "updatedAt")
//    @Mapping(target = "users", ignore = true)
//    RolEntity toEntity(Role role);

//    @Named("mapRoleEntities")
//    default Set<Role> mapRoleEntities(Set<RolEntity> entities) {
//        if (entities == null) {
//            return new HashSet<>();
//        }
//        return entities.stream()
//                .map(this::toRole)
//                .collect(Collectors.toSet());
//    }
//
//    @Named("mapRoles")
//    default Set<RolEntity> mapRoles(Set<Role> roles) {
//        if (roles == null) {
//            return new HashSet<>();
//        }
//        return roles.stream()
//                .map(this::toEntity)
//                .collect(Collectors.toSet());
//    }
//
//    @Named("mapRoleEntity")
//    default Role mapRoleEntity(RolEntity entity) {
//        if (entity == null) {
//            return null;
//        }
//        return toRole(entity);
//    }
}