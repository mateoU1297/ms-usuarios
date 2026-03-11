package com.pragma.users.application.handler.impl;

import com.pragma.users.application.dto.EmployeeRequest;
import com.pragma.users.application.dto.EmployeeResponse;
import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;
import com.pragma.users.application.handler.IUserHandler;
import com.pragma.users.application.mapper.IEmployeeResponseMapper;
import com.pragma.users.application.mapper.IJwtResponseMapper;
import com.pragma.users.application.mapper.IOwnerResponseMapper;
import com.pragma.users.domain.api.IAuthenticationServicePort;
import com.pragma.users.domain.api.IUserServicePort;
import com.pragma.users.domain.model.AuthResult;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.enums.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {

    private final IJwtResponseMapper jwtResponseMapper;
    private final IOwnerResponseMapper ownerResponseMapper;
    private final IEmployeeResponseMapper employeeResponseMapper;

    private final IUserServicePort userServicePort;
    private final IAuthenticationServicePort authenticationServicePort;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        AuthResult authResult = authenticationServicePort.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        return jwtResponseMapper.toJwtResponse(authResult);
    }

    public OwnerResponse createOwner(OwnerRequest ownerRequest) {
        User userModel = ownerResponseMapper.toModel(ownerRequest);
        User savedUser = userServicePort.save(userModel, RoleName.OWNER);
        return ownerResponseMapper.toResponse(savedUser);
    }

    @Override
    public OwnerResponse getUserById(Long userId) {
        return ownerResponseMapper.toResponse(userServicePort.findById(userId));
    }

    @Override
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        User userModel = employeeResponseMapper.toModel(employeeRequest);
        User savedUser = userServicePort.saveEmployee(userModel);
        return employeeResponseMapper.toResponse(savedUser);
    }

}
