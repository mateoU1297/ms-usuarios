package com.pragma.users.domain.api;

import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.enums.RoleName;

public interface IUserServicePort {

    User findByEmail(String email);

    User save(User user, RoleName roleName);

    User findById(Long id);

    User saveEmployee(User user);
}
