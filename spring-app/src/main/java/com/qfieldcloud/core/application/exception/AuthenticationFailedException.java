package com.qfieldcloud.core.application.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends QFieldCloudException {

    public AuthenticationFailedException(String message) {
        super(message, "authentication_failed", HttpStatus.UNAUTHORIZED.value(), false);
    }

    public AuthenticationFailedException() {
        this("Authentication failed");
    }
}