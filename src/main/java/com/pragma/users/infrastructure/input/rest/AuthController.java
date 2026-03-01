package com.pragma.users.infrastructure.input.rest;

import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.handler.IUserHandler;
import com.pragma.users.infrastructure.adapter.in.rest.api.AuthenticationApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthenticationApi {

    private final IUserHandler userHandler;

    @Override
    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {
        JwtResponse response = userHandler.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
}
