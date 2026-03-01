package com.pragma.users.domain.spi;

import com.pragma.users.domain.model.User;

public interface IJwtPort {

    String generateToken(User user);
}
