package com.pragma.usuarios.infrastructure.mapper;

import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserRoleEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserRolePK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IUserRoleEntityMapper {

    @Mapping(target = "userId", source = "id.userId")
    @Mapping(target = "roleId", source = "id.roleId")
    UserRole toDomain(UserRoleEntity entity);

    @Mapping(target = "id", source = ".", qualifiedByName = "mapToPK")
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "assignedAt", source = "assignedAt")
    UserRoleEntity toEntity(UserRole domain);

    @Named("mapToPK")
    default UserRolePK mapToPK(UserRole domain) {

        if (domain == null) {
            return null;
        }

        UserRolePK pk = new UserRolePK();
        pk.setUserId(domain.getUserId());
        pk.setRoleId(domain.getRoleId());

        return pk;
    }

}