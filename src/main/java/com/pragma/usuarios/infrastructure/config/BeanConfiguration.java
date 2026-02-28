package com.pragma.usuarios.infrastructure.config;

import com.pragma.usuarios.domain.api.IAuthenticationServicePort;
import com.pragma.usuarios.domain.api.IJwtServicePort;
import com.pragma.usuarios.domain.api.IUserRoleServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.spi.IAuthenticationPort;
import com.pragma.usuarios.domain.spi.IJwtPort;
import com.pragma.usuarios.domain.spi.IUserPersistencePort;
import com.pragma.usuarios.domain.spi.IUserRolePersistencePort;
import com.pragma.usuarios.domain.usecase.AuthenticationUseCase;
import com.pragma.usuarios.domain.usecase.JwtUseCase;
import com.pragma.usuarios.domain.usecase.UserRoleUseCase;
import com.pragma.usuarios.domain.usecase.UserUseCase;
import com.pragma.usuarios.infrastructure.config.security.JwtUtils;
import com.pragma.usuarios.infrastructure.config.security.adapter.AuthenticationAdapter;
import com.pragma.usuarios.infrastructure.config.security.adapter.JwtAdapter;
import com.pragma.usuarios.infrastructure.mapper.IUserEntityMapper;
import com.pragma.usuarios.infrastructure.mapper.IUserRoleEntityMapper;
import com.pragma.usuarios.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.pragma.usuarios.infrastructure.out.jpa.adapter.UserRoleJpaAdapter;
import com.pragma.usuarios.infrastructure.repository.UserRepository;
import com.pragma.usuarios.infrastructure.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final UserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    private final UserRoleRepository userRoleRepository;
    private final IUserRoleEntityMapper userRoleEntityMapper;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort());
    }

    @Bean
    public IAuthenticationPort authenticationPort() {
        return new AuthenticationAdapter(authenticationManager);
    }

    @Bean
    public IAuthenticationServicePort authenticationServicePort() {
        return new AuthenticationUseCase(authenticationPort());
    }

    @Bean
    public IJwtPort jwtPort() {
        return new JwtAdapter(jwtUtils);
    }

    @Bean
    public IJwtServicePort jwtServicePort() {
        return new JwtUseCase(jwtPort());
    }

    @Bean
    public IUserRolePersistencePort userRolePersistencePort() {
        return new UserRoleJpaAdapter(userRoleRepository, userRoleEntityMapper);
    }

    @Bean
    public IUserRoleServicePort userRoleServicePort() {
        return new UserRoleUseCase(userRolePersistencePort());
    }

}
