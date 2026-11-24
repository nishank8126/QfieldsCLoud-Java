package com.qfieldcloud.core.application.exception;

import lombok.Getter;

@Getter
public abstract class QFieldCloudException extends RuntimeException {

    private final String code;
    private final Integer httpStatusCode;
    private final boolean logAsError;

    protected QFieldCloudException(String message, String code, Integer httpStatusCode, boolean logAsError) {
        super(message);
        this.code = code;
        this.httpStatusCode = httpStatusCode;
        this.logAsError = logAsError;
    }
}