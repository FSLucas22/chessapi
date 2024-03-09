package com.lucas.chessapi.exceptions;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends BusinessException {
    public GameNotFoundException(String message, HttpStatus status) {
        super(message, status);
    }

    public GameNotFoundException(String message) {
        this(message, HttpStatus.CONFLICT);
    }
}
