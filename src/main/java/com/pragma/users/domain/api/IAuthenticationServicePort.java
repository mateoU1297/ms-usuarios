package com.pragma.users.domain.api;

import com.pragma.users.domain.model.AuthResult;

public interface IAuthenticationServicePort {

    AuthResult authenticate(String email, String password);
}
