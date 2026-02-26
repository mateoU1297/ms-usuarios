package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.JwtResponse;
import com.pragma.usuarios.application.dto.LoginRequest;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.domain.api.IUserServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;

    private final AuthenticationManager authenticationManager;

    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Authenticating user handler {}", loginRequest.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        }  catch (AuthenticationException e) {
            log.info("Invalid username or password : {}", e.getMessage());
        }

        log.info("paso por aqui {}", loginRequest.getEmail());

        var user = userServicePort.findByEmail(loginRequest.getEmail()).orElseThrow(RuntimeException::new);

        log.info("User {} logged in", user.getEmail());

        var response = new JwtResponse();

        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());

        return response;
    }

}
