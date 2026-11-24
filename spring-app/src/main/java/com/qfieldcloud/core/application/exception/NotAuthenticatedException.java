package com.qfieldcloud.core.application.exception;

import org.springframework.http.HttpStatus;

public class NotAuthenticatedException extends QFieldCloudException {

    public NotAuthenticatedException() {
        super("Not authenticated", "not_authenticated", HttpStatus.UNAUTHORIZED.value(), false);
    }
}