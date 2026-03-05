package com.pragma.users.domain.usecase;

import com.pragma.users.domain.api.IAuthenticationServicePort;
import com.pragma.users.domain.model.AuthResult;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.IAuthenticationPort;
import com.pragma.users.domain.spi.IJwtPort;
import com.pragma.users.domain.spi.IUserPersistencePort;

public class AuthenticationUseCase implements IAuthenticationServicePort {

    private final IAuthenticationPort authenticationPort;
    private final IUserPersistencePort userPersistencePort;
    private final IJwtPort jwtPort;

    public AuthenticationUseCase(IAuthenticationPort authenticationPort,  IUserPersistencePort userPersistencePort,
                                 IJwtPort jwtPort) {
        this.authenticationPort = authenticationPort;
        this.userPersistencePort = userPersistencePort;
        this.jwtPort = jwtPort;
    }

    @Override
    public AuthResult authenticate(String email, String password) {
        authenticationPort.authenticate(email, password);
        User user = userPersistencePort.findByEmail(email);
        String token = jwtPort.generateToken(user);
        return new AuthResult(token, user);
    }

}
