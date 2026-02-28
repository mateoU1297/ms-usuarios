package com.pragma.usuarios.application.handler;

import com.pragma.usuarios.application.dto.JwtResponse;
import com.pragma.usuarios.application.dto.LoginRequest;
import com.pragma.usuarios.application.dto.OwnerRequest;
import com.pragma.usuarios.application.dto.OwnerResponse;

public interface IUserHandler {

    JwtResponse authenticate(LoginRequest loginRequest);
    OwnerResponse createOwner(OwnerRequest ownerRequest);
}
