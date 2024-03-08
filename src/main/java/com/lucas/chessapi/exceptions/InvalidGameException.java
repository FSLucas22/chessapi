package com.lucas.chessapi.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidGameException extends BusinessException {
    public InvalidGameException(String message, HttpStatus status) {
        super(message, status);
    }

    public InvalidGameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
