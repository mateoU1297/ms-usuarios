package com.pragma.users.application.handler;

import com.pragma.users.application.dto.EmployeeRequest;
import com.pragma.users.application.dto.EmployeeResponse;
import com.pragma.users.application.dto.JwtResponse;
import com.pragma.users.application.dto.LoginRequest;
import com.pragma.users.application.dto.OwnerRequest;
import com.pragma.users.application.dto.OwnerResponse;

public interface IUserHandler {

    JwtResponse authenticate(LoginRequest loginRequest);

    OwnerResponse createOwner(OwnerRequest ownerRequest);

    OwnerResponse getUserById(Long userId);

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
}
