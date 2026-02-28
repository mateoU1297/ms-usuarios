package com.pragma.usuarios.application.handler.impl;

import com.pragma.usuarios.application.dto.JwtResponse;
import com.pragma.usuarios.application.dto.LoginRequest;
import com.pragma.usuarios.application.dto.OwnerRequest;
import com.pragma.usuarios.application.dto.OwnerResponse;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.application.mapper.IJwtResponseMapper;
import com.pragma.usuarios.application.mapper.IOwnerResponseMapper;
import com.pragma.usuarios.domain.api.IAuthenticationServicePort;
import com.pragma.usuarios.domain.api.IJwtServicePort;
import com.pragma.usuarios.domain.api.IUserServicePort;
import com.pragma.usuarios.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final IJwtResponseMapper jwtResponseMapper;
    private final IOwnerResponseMapper ownerResponseMapper;

    private final IJwtServicePort jwtServicePort;
    private final IUserServicePort userServicePort;
    private final IAuthenticationServicePort authenticationServicePort;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        authenticationServicePort.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        User user = userServicePort.findByEmail(loginRequest.getEmail());

        JwtResponse response = jwtResponseMapper.toJwtResponse(user);
        response.setToken(jwtServicePort.generateToken(user));

        return response;
    }

    public OwnerResponse createOwner(OwnerRequest ownerRequest) {
        User entity = ownerResponseMapper.toModel(ownerRequest);
        return ownerResponseMapper.toResponse(userServicePort.save(entity));
    }

}
