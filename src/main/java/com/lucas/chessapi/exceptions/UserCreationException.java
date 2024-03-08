package com.lucas.chessapi.exceptions;

import org.springframework.http.HttpStatus;

public class UserCreationException extends BusinessException {
    public UserCreationException(String message) {
        this(message, HttpStatus.CONFLICT);
    }

    public UserCreationException(String message, HttpStatus status) {
        super(message, status);
    }
}
