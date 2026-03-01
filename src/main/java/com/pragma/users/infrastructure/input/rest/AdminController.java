package com.pragma.users.infrastructure.input.rest;

import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;
import com.pragma.users.application.handler.IUserHandler;
import com.pragma.users.infrastructure.adapter.in.rest.api.AdminApi;
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
