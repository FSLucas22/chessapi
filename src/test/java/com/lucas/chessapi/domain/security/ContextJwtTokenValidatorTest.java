package com.lucas.chessapi.domain.security;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.security.jwt.JwtTokenValidator;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContextJwtTokenValidatorTest extends TestContextHelper {
    @Mock
    protected Jws<Claims> claimsJws;

    @Mock
    protected JwtParser parser;

    @InjectMocks
    JwtTokenValidator validator;

    protected void givenJwtParserReturnsClaims(Claims claims, String forToken) {
        when(parser.parseClaimsJws(forToken)).thenReturn(claimsJws);
        when(claimsJws.getBody()).thenReturn(claims);
    }

    protected void givenJwtParserThrows(JwtException exception, String forToken) {
        when(parser.parseClaimsJws(forToken)).thenThrow(exception);
    }

    protected void whenValidationIsMadeFor(String token) {
        try {
            validator.validate(token);
        } catch (Exception e) {
            error = e;
        }
    }
}
