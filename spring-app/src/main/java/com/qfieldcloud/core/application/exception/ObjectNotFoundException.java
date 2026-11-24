package com.qfieldcloud.core.application.exception;

import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends QFieldCloudException {

    public ObjectNotFoundException(String message) {
        super(message, "object_not_found", HttpStatus.NOT_FOUND.value(), false);
    }

    public ObjectNotFoundException() {
        this("Object not found");
    }
}