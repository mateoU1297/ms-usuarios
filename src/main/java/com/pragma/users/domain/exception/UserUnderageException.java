package com.pragma.users.domain.exception;

import java.util.Map;

public class UserUnderageException extends DomainException {

    public UserUnderageException() {
        super("USER_UNDERAGE", "User must be at least 18 years old");
    }

    public UserUnderageException(Integer age) {
        super("USER_UNDERAGE",
                String.format("User must be at least 18 years old (current age: %d)", age),
                Map.of("currentAge", age, "minimumAge", 18));
    }
}

