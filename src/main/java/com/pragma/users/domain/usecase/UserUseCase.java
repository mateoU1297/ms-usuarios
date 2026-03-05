package com.pragma.users.domain.usecase;

import com.pragma.users.domain.api.IUserServicePort;
import com.pragma.users.domain.exception.UserUnderageException;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.UserRole;
import com.pragma.users.domain.model.enums.RoleName;
import com.pragma.users.domain.spi.IAuthenticationPort;
import com.pragma.users.domain.spi.IRolePersistencePort;
import com.pragma.users.domain.spi.IUserPersistencePort;
import com.pragma.users.domain.spi.IUserRolePersistencePort;

import java.util.Set;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IAuthenticationPort authenticationPort;
    private final IRolePersistencePort rolePersistencePort;
    private final IUserRolePersistencePort userRolePersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IAuthenticationPort authenticationPort,
                       IRolePersistencePort rolePersistencePort, IUserRolePersistencePort userRolePersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.authenticationPort = authenticationPort;
        this.rolePersistencePort = rolePersistencePort;
        this.userRolePersistencePort = userRolePersistencePort;
    }

    @Override
    public User findByEmail(String email) {
        return userPersistencePort.findByEmail(email);
    }

    @Override
    public User save(User user, RoleName roleName) {
        if (!user.isAdult())
            throw new UserUnderageException();

        user.setPassword(authenticationPort.encode(user.getPassword()));

        User savedUser = userPersistencePort.save(user);

        Role role = rolePersistencePort.getRoleByName(roleName);
        UserRole userRole = new UserRole();
        userRole.setUserId(savedUser.getId());
        userRole.setRoleId(role.getId());
        userRolePersistencePort.save(userRole);

        savedUser.setRoles(Set.of(role));
        return savedUser;
    }

    @Override
    public User findById(Long id) {
        return userPersistencePort.findById(id);
    }
}
