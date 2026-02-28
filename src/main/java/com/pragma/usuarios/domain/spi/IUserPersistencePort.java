package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.User;

public interface IUserPersistencePort {

    User findByEmail(String email);
    User save(User user);
}
