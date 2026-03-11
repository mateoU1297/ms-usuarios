package com.pragma.users.application.handler;

import com.pragma.users.application.dto.ClientRequest;
import com.pragma.users.application.dto.ClientResponse;
import com.pragma.users.application.dto.EmployeeRequest;
import com.pragma.users.application.dto.EmployeeResponse;
import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;

public interface IUserHandler {

    JwtResponse authenticate(LoginRequest loginRequest);

    OwnerResponse getUserById(Long userId);

    OwnerResponse createOwner(OwnerRequest ownerRequest);

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    ClientResponse createClient(ClientRequest clientRequest);
}
