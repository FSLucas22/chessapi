package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.domain.security.ContextJwtTokenValidatorTest;
import com.lucas.chessapi.exceptions.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.builders.JwtTokenDtoFactory.jwtTokenDtoWithSubject;

public class JwtTokenValidatorTest extends ContextJwtTokenValidatorTest {
    private final String token = "1234";

    @Test
    void shouldDoNothingWhenTokenIsValid() {
        var claims = jwtTokenDtoWithSubject("1").toClaims();
        givenJwtParserReturnsClaims(claims, token);
        whenValidationIsMadeFor(token);
        thenNothingHappens();
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenSubjectIsNotLong() {
        var claims = jwtTokenDtoWithSubject("abc").toClaims();

        givenJwtParserReturnsClaims(claims, token);
        whenValidationIsMadeFor(token);
        thenShouldThrow(InvalidTokenException.class, "Subject must be a valid Long");
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenParserThrowsJwtException() {
        JwtException exception = new MalformedJwtException("test");
        givenJwtParserThrows(exception, token);
        whenValidationIsMadeFor(token);
        thenShouldThrow(InvalidTokenException.class, "test");
    }
}