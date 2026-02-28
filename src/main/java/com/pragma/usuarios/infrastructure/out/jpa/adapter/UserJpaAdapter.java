package com.pragma.usuarios.infrastructure.out.jpa.adapter;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.pragma.usuarios.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final UserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("email not found"));

        return userEntityMapper.toUser(userEntity);
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userRepository.save(userEntityMapper.toEntity(user));
        return userEntityMapper.toUser(userEntity);
    }
}
