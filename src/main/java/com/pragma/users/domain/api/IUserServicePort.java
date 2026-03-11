package com.pragma.users.domain.api;

import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.enums.RoleName;

public interface IUserServicePort {

    User findByEmail(String email);

    User saveOwner(User user);

    User findById(Long id);

    User saveEmployee(User user);

    User saveClient(User user);
}
