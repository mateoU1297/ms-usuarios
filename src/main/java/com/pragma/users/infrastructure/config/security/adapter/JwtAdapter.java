package com.pragma.users.infrastructure.config.security.adapter;

import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.IJwtPort;
import com.pragma.users.infrastructure.config.security.JwtUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAdapter implements IJwtPort {

    private final JwtUtils jwtUtils;

    @Override
    public String generateToken(User user) {
        return jwtUtils.generateJwtToken(user);
    }
}
