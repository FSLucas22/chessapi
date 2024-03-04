package com.lucas.chessapi.exceptions;

public class InvalidJwtDto extends RuntimeException {
    public InvalidJwtDto(String message) {
        super(message);
    }
}
