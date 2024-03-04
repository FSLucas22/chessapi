package com.lucas.chessapi.unit;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.domain.JwtCreatorContext;
import com.lucas.chessapi.exceptions.ExpiredTokenException;
import com.lucas.chessapi.exceptions.InvalidJwtDto;
import com.lucas.chessapi.security.jwt.JwtCreator;
import com.lucas.chessapi.security.jwt.JwtDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class JwtCreatorTest extends JwtCreatorContext {
    @BeforeEach
    void setUp() {
        securityConfiguration = new SecurityConfiguration(
                "Bearer",
                "SECRET_KEY",
                36000L
        );
        jwtCreator = new JwtCreator(securityConfiguration);
    }

    @Test
    void shouldCreateToken() {
        givenValidJwtDto();
        whenTokenIsGenerated();
        thenParsedClaimsFieldsShouldMatch();
    }

    @Test
    void shouldThrowInvalidJwtDtoWhenSubjectIsBlank() {
        var jwtDtoWithBlankSuject = new JwtDto(" ", new Date(), new Date());

        given(jwtDtoWithBlankSuject);
        whenTokenIsGenerated();
        thenShouldThrow(InvalidJwtDto.class, "Subject cannot be blank");
    }

    @Test
    void shouldThrowInvalidJwtDtoWhenSubjectIsNull() {
        var jwtDtoWithNullSubject = new JwtDto(null, new Date(), new Date());

        given(jwtDtoWithNullSubject);
        whenTokenIsGenerated();
        thenShouldThrow(InvalidJwtDto.class, "Subject cannot be null");
    }

    @Test
    void shouldThrowInvalidJwtDtoWhenIssueDateAtIsNull() {
        var jwtDtoWithNullIssueDateDate = new JwtDto("1", null, new Date());

        given(jwtDtoWithNullIssueDateDate);
        whenTokenIsGenerated();
        thenShouldThrow(InvalidJwtDto.class, "Issue date cannot be null");
    }

    @Test
    void shouldThrowInvalidJwtDtoWhenExpirationIsNull() {
        var jwtDtoWithNullExpirationDate = new JwtDto("1", new Date(), null);

        given(jwtDtoWithNullExpirationDate);
        whenTokenIsGenerated();
        thenShouldThrow(InvalidJwtDto.class, "Expiration date cannot be null");
    }

    @Test
    void shouldRecoverDataFromValidToken() {
        given(validDto());
        whenTokenIsGenerated();
        thenShouldRecoverDataFromToken();
    }

    @Test
    void shouldThrowExpiredTokenExceptionWhenTokenIsExpired() {
        given(expiredToken());
        whenJwtDtoIsRecovered();
        thenShouldThrow(ExpiredTokenException.class, "Token is expired");
    }
}
