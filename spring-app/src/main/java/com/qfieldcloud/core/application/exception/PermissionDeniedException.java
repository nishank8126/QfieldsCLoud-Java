package com.qfieldcloud.core.application.exception;

import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends QFieldCloudException {

    public PermissionDeniedException(String message) {
        super(message, "permission_denied", HttpStatus.FORBIDDEN.value(), false);
    }

    public PermissionDeniedException() {
        this("Permission denied");
    }
}