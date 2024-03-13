package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.exceptions.InvalidTokenException;
import com.lucas.chessapi.security.jwt.JwtTokenValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.lucas.chessapi.builders.JwtTokenDtoFactory.jwtTokenDtoWithSubject;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenValidatorTest {
    @Mock
    Jws<Claims> claimsJws;

    @Mock
    JwtParser parser;

    @InjectMocks
    JwtTokenValidator validator;

    final String token = "1234";

    @Test
    void shouldDoNothingWhenTokenIsValid() {
        // GIVEN
        var claims = jwtTokenDtoWithSubject("1").toClaims();
        when(parser.parseClaimsJws(token)).thenReturn(claimsJws);
        when(claimsJws.getBody()).thenReturn(claims);

        // WHEN
        validator.validate(token);

        // THEN
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenSubjectIsNotLong() {
        // GIVEN
        var claims = jwtTokenDtoWithSubject("abc").toClaims();

        when(parser.parseClaimsJws(token)).thenReturn(claimsJws);
        when(claimsJws.getBody()).thenReturn(claims);

        // WHEN
        assertThatThrownBy(() -> validator.validate(token))
                // THEN
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Subject must be a valid Long");
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenParserThrowsJwtException() {
        // GIVEN
        when(parser.parseClaimsJws(token)).thenThrow(new MalformedJwtException("test"));

        // WHEN
        assertThatThrownBy(() -> validator.validate(token))
                // THEN
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("test");
    }
}
