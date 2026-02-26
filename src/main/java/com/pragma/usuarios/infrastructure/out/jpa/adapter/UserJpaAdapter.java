package com.pragma.usuarios.infrastructure.out.jpa.adapter;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.infrastructure.entity.UserEntity;
import com.pragma.usuarios.infrastructure.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final UserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    public Optional<User> findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("email not found"));

        return Optional.ofNullable(userEntityMapper.toUser(userEntity));
    }
}
