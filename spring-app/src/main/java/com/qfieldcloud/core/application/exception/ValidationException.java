package com.qfieldcloud.core.application.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends QFieldCloudException {

    public ValidationException(String message) {
        super(message, "validation_error", HttpStatus.BAD_REQUEST.value(), false);
    }

    public ValidationException() {
        this("Validation error");
    }
}