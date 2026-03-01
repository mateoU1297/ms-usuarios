package com.pragma.users.domain.usecase;

import com.pragma.users.domain.api.IRoleServicePort;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.enums.RoleName;
import com.pragma.users.domain.spi.IRolePersistencePort;

public class RoleUseCase implements IRoleServicePort {

    private final IRolePersistencePort rolePersistencePort;

    public RoleUseCase(IRolePersistencePort rolePersistencePort) {
        this.rolePersistencePort = rolePersistencePort;
    }

    @Override
    public Role getRoleByName(RoleName roleName) {
        return rolePersistencePort.getRoleByName(roleName);
    }
}
