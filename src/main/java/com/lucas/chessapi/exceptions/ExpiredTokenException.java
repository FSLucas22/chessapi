package com.lucas.chessapi.exceptions;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;

public class ExpiredTokenException extends ExpiredJwtException {
    public ExpiredTokenException(Header header, Claims claims, String message) {
        super(header, claims, message);
    }
}
