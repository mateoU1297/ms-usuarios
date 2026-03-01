package com.pragma.users.domain.api;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.enums.RoleName;

public interface IRoleServicePort {

    Role getRoleByName(RoleName roleName);
}
