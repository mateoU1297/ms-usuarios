package com.pragma.users.domain.usecase;

import com.pragma.users.domain.api.IAuthenticationServicePort;
import com.pragma.users.domain.spi.IAuthenticationPort;

public class AuthenticationUseCase implements IAuthenticationServicePort {

    private final IAuthenticationPort authenticationPort;

    public AuthenticationUseCase(IAuthenticationPort authenticationPort) {
        this.authenticationPort = authenticationPort;
    }

    @Override
    public void authenticate(String email, String password) {
        authenticationPort.authenticate(email, password);
    }

    @Override
    public String encode(String rawPassword) {
        return authenticationPort.encode(rawPassword);
    }
}
