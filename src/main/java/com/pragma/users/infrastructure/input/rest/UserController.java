package com.pragma.users.infrastructure.input.rest;

import com.pragma.users.application.dto.ClientRequest;
import com.pragma.users.application.dto.ClientResponse;
import com.pragma.users.application.dto.EmployeeRequest;
import com.pragma.users.application.dto.EmployeeResponse;
import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;
import com.pragma.users.application.handler.IUserHandler;
import com.pragma.users.infrastructure.adapter.in.rest.api.UsersApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {

    private final IUserHandler userHandler;

    @Override
    public ResponseEntity<OwnerResponse> getUserById(Long userId) {
        OwnerResponse response = userHandler.getUserById(userId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OwnerResponse> createOwner(OwnerRequest ownerRequest) {
        OwnerResponse response = userHandler.createOwner(ownerRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<EmployeeResponse> createEmployee(EmployeeRequest employeeRequest) {
        EmployeeResponse response = userHandler.createEmployee(employeeRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ClientResponse> createClient(ClientRequest clientRequest) {
        ClientResponse response = userHandler.createClient(clientRequest);
        return ResponseEntity.ok(response);
    }

}
