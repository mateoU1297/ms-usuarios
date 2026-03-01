package com.pragma.users.application.exception;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final Object details;

    public ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApplicationException(String errorCode, String message, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }
}

