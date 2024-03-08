package com.lucas.chessapi.exceptions;

import org.springframework.http.HttpStatus;

public class GetUserException extends BusinessException {
    public GetUserException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public GetUserException(String message, HttpStatus status) {
        super(message, status);
    }
}
