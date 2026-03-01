package com.pragma.users.domain.spi;

import com.pragma.users.domain.model.User;

public interface IUserPersistencePort {

    User findByEmail(String email);

    User save(User user);
}
