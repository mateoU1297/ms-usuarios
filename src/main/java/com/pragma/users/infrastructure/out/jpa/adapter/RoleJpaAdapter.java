package com.pragma.users.infrastructure.out.jpa.adapter;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.enums.RoleName;
import com.pragma.users.domain.spi.IRolePersistencePort;
import com.pragma.users.infrastructure.mapper.IRoleEntityMapper;
import com.pragma.users.infrastructure.repository.RoleRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final RoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public Role getRoleByName(RoleName roleName) {
        return roleEntityMapper.toRole(roleRepository.findByName(roleName));
    }
}
