package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.JwtResponse;
import com.pragma.usuarios.application.dto.LoginRequest;

public interface IUserHandler {

    JwtResponse login(LoginRequest loginRequest);
}
