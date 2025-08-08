package com.github.alideweb.stuffshop.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseHttpException {
    public UserNotFoundException(String username) {
        super(HttpStatus.NOT_FOUND, "ERR_USER_NOT_FOUND", "user with username " + username + " not found");
    }

    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "ERR_USER_NOT_FOUND", "user with id " + id + " not found");
    }
}
