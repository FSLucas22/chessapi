package com.lucas.chessapi.security.jwt;

import com.lucas.chessapi.exceptions.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenValidator {
    private final JwtParser parser;

    public void validate(String token) {
        try {
            var values = parser.parseClaimsJws(token).getBody();
            Long.parseLong(values.getSubject());
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("Subject must be a valid Long");
        }
    }
}
