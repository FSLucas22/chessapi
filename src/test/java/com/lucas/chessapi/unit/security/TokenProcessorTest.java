package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.DateFactory;
import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.domain.security.TokenProcessorContext;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenProcessorTest extends TokenProcessorContext {
    @BeforeEach
    void setUp() {
        securityConfiguration = new SecurityConfiguration(
                "Bearer",
                "SECRET_KEY",
                36000L
        );
        tokenProcessor = new TokenProcessor(securityConfiguration);
    }

    @Test
    void shouldIssueTokenWithCorrectExpirationDate() {
        var issueDate = DateFactory.today();
        var expirationDate = DateFactory.expirationDate(issueDate, securityConfiguration.expiration());

        givenSubject("123");
        whenIssueTokenIsCalled(issueDate);
        thenParsedClaimsFieldsShouldMatch(
                Jwts.claims()
                        .setSubject("123")
                        .setIssuedAt(issueDate)
                        .setExpiration(expirationDate)
        );
    }

    @Test
    void shouldRecoverDataFromToken() {
        givenSubject("123");
        whenIssueTokenIsCalled(DateFactory.today());
        thenShouldRecoverDataFromToken();
    }
}
