package com.pragma.users.domain.exception;

import java.util.Map;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String field, String value) {
        super("USER_ALREADY_EXISTS",
                String.format("User with %s '%s' already exists", field, value),
                Map.of("field", field, "value", value));
    }
}
