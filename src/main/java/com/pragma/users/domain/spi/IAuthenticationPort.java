package com.pragma.users.domain.spi;

public interface IAuthenticationPort {

    void authenticate(String email, String password);

    String encode(String rawPassword);
}
