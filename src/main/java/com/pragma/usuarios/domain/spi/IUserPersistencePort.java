package com.pragma.usuarios.domain.spi;

import com.pragma.usuarios.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {

    Optional<User> findByEmail(String email);
}
