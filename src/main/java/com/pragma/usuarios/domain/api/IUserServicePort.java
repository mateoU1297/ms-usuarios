package com.pragma.usuarios.domain.api;

import com.pragma.usuarios.domain.model.User;

public interface IUserServicePort {

    User findByEmail(String email);
    User save(User user);
}
