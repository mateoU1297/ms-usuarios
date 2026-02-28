package com.pragma.usuarios.domain.usecase;

import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public User findByEmail(String email) {
        return userPersistencePort.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userPersistencePort.save(user);
    }
}
