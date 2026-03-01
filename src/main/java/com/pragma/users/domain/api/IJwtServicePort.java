package com.pragma.users.domain.api;

import com.pragma.users.domain.model.User;

public interface IJwtServicePort {

    String generateToken(User user);
}
