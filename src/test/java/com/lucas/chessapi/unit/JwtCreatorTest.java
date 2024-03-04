package com.lucas.chessapi.unit;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.domain.JwtCreatorContext;
import com.lucas.chessapi.security.jwt.JwtCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
