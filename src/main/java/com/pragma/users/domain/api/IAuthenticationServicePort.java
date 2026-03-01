package com.pragma.users.domain.api;

public interface IAuthenticationServicePort {

    void authenticate(String email, String password);

    String encode(String rawPassword);
}
