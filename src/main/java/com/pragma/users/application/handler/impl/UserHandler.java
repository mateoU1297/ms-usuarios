package com.pragma.users.application.handler.impl;

import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;
import com.pragma.users.application.handler.IUserHandler;
import com.pragma.users.application.mapper.IJwtResponseMapper;
import com.pragma.users.application.mapper.IOwnerResponseMapper;
import com.pragma.users.domain.api.IAuthenticationServicePort;
import com.pragma.users.domain.api.IJwtServicePort;
import com.pragma.users.domain.api.IRoleServicePort;
import com.pragma.users.domain.api.IUserRoleServicePort;
import com.pragma.users.domain.api.IUserServicePort;
import com.pragma.users.domain.model.Role;
import com.pragma.users.domain.model.User;
import com.pragma.users.domain.model.UserRole;
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

    private final IJwtServicePort jwtServicePort;
    private final IUserServicePort userServicePort;
    private final IRoleServicePort roleServicePort;
    private final IUserRoleServicePort userRoleServicePort;
    private final IAuthenticationServicePort authenticationServicePort;

    public JwtResponse authenticate(LoginRequest loginRequest) {
        authenticationServicePort.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        User user = userServicePort.findByEmail(loginRequest.getEmail());

        JwtResponse response = jwtResponseMapper.toJwtResponse(user);
        response.setToken(jwtServicePort.generateToken(user));

        return response;
    }

    public OwnerResponse createOwner(OwnerRequest ownerRequest) {
        User userModel = ownerResponseMapper.toModel(ownerRequest);
        userModel.setPassword(authenticationServicePort.encode(ownerRequest.getPassword()));
        OwnerResponse ownerResponse = ownerResponseMapper.toResponse(userServicePort.save(userModel));
        Role role = roleServicePort.getRoleByName(RoleName.OWNER);
        UserRole userRole = new UserRole();
        userRole.setUserId(ownerResponse.getId());
        userRole.setRoleId(role.getId());
        userRoleServicePort.save(userRole);
        ownerResponse.addRolesItem(role.getName().name());
        return ownerResponse;
    }

}
