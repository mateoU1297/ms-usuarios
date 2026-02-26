package com.pragma.usuarios.infrastructure.config.security;

import com.pragma.usuarios.domain.model.User;
import com.pragma.usuarios.infrastructure.entity.UserEntity;
import com.pragma.usuarios.infrastructure.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        log.info("User found: {}, roles size: {}",
                userEntity.getEmail(),
                userEntity.getRoles() != null ? userEntity.getRoles().size() : 0);

        User user = userEntityMapper.toUser(userEntity);

        if (user.getRoles() == null) {
            log.error("Roles are null for user: {}", email);
            throw new UsernameNotFoundException("User has no roles assigned: " + email);
        }

        return UserDetailsImpl.build(user);

    }
}