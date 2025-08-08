package com.github.alideweb.stuffshop.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseHttpException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    protected BaseHttpException(HttpStatus status, String errorCode, String message) {
        super(message);

        this.status = status;
        this.errorCode = errorCode;
    }
}
