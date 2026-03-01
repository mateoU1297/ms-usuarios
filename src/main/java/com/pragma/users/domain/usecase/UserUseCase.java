package com.pragma.users.domain.usecase;

import com.pragma.users.domain.api.IUserServicePort;
import com.pragma.users.domain.exception.UserUnderageException;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.spi.IUserPersistencePort;

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
        if (!user.isAdult())
            throw new UserUnderageException();

        return userPersistencePort.save(user);
    }
}
