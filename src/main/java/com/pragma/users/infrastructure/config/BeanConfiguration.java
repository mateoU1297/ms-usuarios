package com.pragma.users.infrastructure.config;

import com.pragma.users.domain.api.IAuthenticationServicePort;
import com.pragma.users.domain.api.IUserServicePort;
import com.pragma.users.domain.spi.IAuthenticationPort;
import com.pragma.users.domain.spi.IJwtPort;
import com.pragma.users.domain.spi.IRolePersistencePort;
import com.pragma.users.domain.spi.IUserPersistencePort;
import com.pragma.users.domain.spi.IUserRolePersistencePort;
import com.pragma.users.domain.usecase.AuthenticationUseCase;
import com.pragma.users.domain.usecase.UserUseCase;
import com.pragma.users.infrastructure.config.security.JwtUtils;
import com.pragma.users.infrastructure.config.security.adapter.AuthenticationAdapter;
import com.pragma.users.infrastructure.config.security.adapter.JwtAdapter;
import com.pragma.users.infrastructure.mapper.IRoleEntityMapper;
import com.pragma.users.infrastructure.mapper.IUserEntityMapper;
import com.pragma.users.infrastructure.mapper.IUserRoleEntityMapper;
import com.pragma.users.infrastructure.out.jpa.adapter.RoleJpaAdapter;
import com.pragma.users.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.pragma.users.infrastructure.out.jpa.adapter.UserRoleJpaAdapter;
import com.pragma.users.infrastructure.repository.RoleRepository;
import com.pragma.users.infrastructure.repository.UserRepository;
import com.pragma.users.infrastructure.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final UserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    private final UserRoleRepository userRoleRepository;
    private final IUserRoleEntityMapper userRoleEntityMapper;

    private final RoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort(), authenticationPort(), rolePersistencePort(), userRolePersistencePort());
    }

    @Bean
    public IAuthenticationPort authenticationPort() {
        return new AuthenticationAdapter(passwordEncoder, authenticationManager);
    }

    @Bean
    public IAuthenticationServicePort authenticationServicePort() {
        return new AuthenticationUseCase(authenticationPort(), userPersistencePort(), jwtPort());
    }

    @Bean
    public IJwtPort jwtPort() {
        return new JwtAdapter(jwtUtils);
    }

    @Bean
    public IUserRolePersistencePort userRolePersistencePort() {
        return new UserRoleJpaAdapter(userRoleRepository, userRepository, roleRepository, userRoleEntityMapper);
    }

    @Bean
    public IRolePersistencePort rolePersistencePort() {
        return new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }

}
