package com.pragma.users.domain.api;

import com.pragma.users.domain.model.UserRole;

public interface IUserRoleServicePort {

    UserRole save(UserRole userRole);
}
