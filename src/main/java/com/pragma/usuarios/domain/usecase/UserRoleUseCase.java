package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.model.UserRole;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;

public class UserRoleUseCase implements IUserRoleServicePort {

    private final IUserRolePersistencePort userRolePersistencePort;

    public UserRoleUseCase(IUserRolePersistencePort userRolePersistencePort) {
        this.userRolePersistencePort = userRolePersistencePort;
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRolePersistencePort.save(userRole);
    }
}
