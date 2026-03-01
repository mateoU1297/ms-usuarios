package com.pragma.users.domain.spi;

import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.enums.RoleName;

public interface IRolePersistencePort {

    Role getRoleByName(RoleName roleName);
}
