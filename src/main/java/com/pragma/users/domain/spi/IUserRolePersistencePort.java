package com.pragma.users.domain.spi;

import com.pragma.users.domain.model.UserRole;

public interface IUserRolePersistencePort {

    UserRole save(UserRole userRole);
}
