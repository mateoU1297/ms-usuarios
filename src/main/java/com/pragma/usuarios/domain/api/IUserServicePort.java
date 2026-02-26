package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.User;

import java.util.Optional;

public interface IUserServicePort {

    Optional<User> findByEmail(String email);
}
