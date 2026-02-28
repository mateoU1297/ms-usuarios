package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.UserRole;

public interface IUserRolePersistencePort {

    UserRole save(UserRole userRole);
}
