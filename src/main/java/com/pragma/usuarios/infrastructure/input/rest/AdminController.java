package com.pragma.usuarios.infrastructure.input.rest;

import com.pragma.usuarios.application.dto.OwnerRequest;
import com.pragma.usuarios.application.dto.OwnerResponse;
import com.pragma.usuarios.application.handler.IUserHandler;
import com.pragma.usuarios.infrastructure.adapter.in.rest.api.AdminApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController implements AdminApi {

    private final IUserHandler userHandler;

    @Override
    public ResponseEntity<OwnerResponse> createOwner(OwnerRequest ownerRequest) {
        OwnerResponse response = userHandler.createOwner(ownerRequest);
        return ResponseEntity.ok(response);
    }

}
