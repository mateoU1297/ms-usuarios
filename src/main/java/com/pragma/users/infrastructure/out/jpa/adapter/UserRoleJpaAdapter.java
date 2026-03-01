package com.pragma.users.infrastructure.out.jpa.adapter;

import com.pragma.users.domain.model.UserRole;
import com.pragma.users.domain.spi.IUserRolePersistencePort;
import com.pragma.users.infrastructure.mapper.IUserRoleEntityMapper;
import com.pragma.users.infrastructure.out.jpa.entity.RolEntity;
import com.pragma.users.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.users.infrastructure.out.jpa.entity.UserRoleEntity;
import com.pragma.users.infrastructure.repository.RoleRepository;
import com.pragma.users.infrastructure.repository.UserRepository;
import com.pragma.users.infrastructure.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRoleJpaAdapter implements IUserRolePersistencePort {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final IUserRoleEntityMapper userRoleEntityMapper;

    @Override
    public UserRole save(UserRole userRole) {
        UserRoleEntity userRoleEntity = userRoleEntityMapper.toEntity(userRole);

        UserEntity userEntity = userRepository.getReferenceById(userRole.getUserId());
        RolEntity rolEntity = roleRepository.getReferenceById(userRole.getRoleId());

        userRoleEntity.setUser(userEntity);
        userRoleEntity.setRole(rolEntity);
        return userRoleEntityMapper.toDomain(userRoleRepository.save(userRoleEntity));
    }
}
