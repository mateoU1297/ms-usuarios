package com.pragma.users.domain.usecase;

import com.pragma.users.domain.api.IJwtServicePort;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.IJwtPort;

public class JwtUseCase implements IJwtServicePort {

    private final IJwtPort jwtPort;

    public JwtUseCase(IJwtPort jwtPort) {
        this.jwtPort = jwtPort;
    }

    @Override
    public String generateToken(User user) {
        return jwtPort.generateToken(user);
    }
}
