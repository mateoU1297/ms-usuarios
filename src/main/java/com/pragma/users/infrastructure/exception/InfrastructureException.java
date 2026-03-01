package com.pragma.users.infrastructure.exception;

import lombok.Getter;

@Getter
public abstract class InfrastructureException extends RuntimeException {

    private final String errorCode;
    private final Object details;

    public InfrastructureException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public InfrastructureException(String errorCode, String message, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }
}
