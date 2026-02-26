package com.pragma.usuarios.infrastructure.mapper;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IRoleEntityMapper.class})
public interface IUserEntityMapper {

    User toUser(UserEntity entity);

    UserEntity toEntity(User user);

}