package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.DateFactory;
import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.domain.security.ContextTokenProcessor;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TokenProcessorTest extends ContextTokenProcessor {
    @BeforeEach
    void setUp() {
        securityConfiguration = new SecurityConfiguration();
        securityConfiguration.setPrefix("Bearer");
        securityConfiguration.setKey("SECRET_KEY");
        securityConfiguration.setExpiration(36000L);
        tokenProcessor = new TokenProcessor(securityConfiguration);
    }

    @Test
    void shouldIssueTokenWithCorrectExpirationDate() {
        var issueDate = DateFactory.today();
        var expirationDate = DateFactory.expirationDate(issueDate, securityConfiguration.getExpiration());

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
