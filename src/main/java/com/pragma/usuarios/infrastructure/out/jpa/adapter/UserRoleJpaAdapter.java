package com.pragma.usuarios.infrastructure.out.jpa.adapter;

import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;
import com.pragma.usuarios.infrastructure.mapper.IUserRoleEntityMapper;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserRoleEntity;
import com.pragma.usuarios.infrastructure.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRoleJpaAdapter implements IUserRolePersistencePort {

    private final UserRoleRepository userRoleRepository;
    private final IUserRoleEntityMapper userRoleEntityMapper;

    @Override
    public UserRole save(UserRole userRole) {
        UserRoleEntity userRoleEntity = userRoleEntityMapper.toEntity(userRole);
        return userRoleEntityMapper.toDomain(userRoleRepository.save(userRoleEntity));
    }
}
