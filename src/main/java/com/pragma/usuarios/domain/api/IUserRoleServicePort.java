package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.UserRole;

public interface IUserRoleServicePort {

    UserRole save(UserRole userRole);
}
