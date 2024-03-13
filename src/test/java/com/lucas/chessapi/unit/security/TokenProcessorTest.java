package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.DateFactory;
import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenProcessorTest {
    protected SecurityConfiguration securityConfiguration;
    protected TokenProcessor tokenProcessor;

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
        var token = tokenProcessor.issueToken("123", issueDate);
        var expectedClaims = Jwts.claims()
                .setSubject("123")
                .setIssuedAt(issueDate)
                .setExpiration(expirationDate);

        Claims claims = Jwts.parser()
                .setSigningKey(securityConfiguration.getKey())
                .parseClaimsJws(token).getBody();

        assertThat(claims.getSubject()).isEqualTo(expectedClaims.getSubject());
        assertThat(claims.getIssuedAt()).isEqualTo(expectedClaims.getIssuedAt());
        assertThat(claims.getExpiration()).isEqualTo(expectedClaims.getExpiration());
    }

    @Test
    void shouldRecoverDataFromToken() {
        var token = tokenProcessor.issueToken("123", DateFactory.today());
        var dto = tokenProcessor.getJwtTokenDtoFromToken(token);
        var expectedClaims = dto.toClaims();

        Claims claims = Jwts.parser()
                .setSigningKey(securityConfiguration.getKey())
                .parseClaimsJws(token).getBody();

        assertThat(claims.getSubject()).isEqualTo(expectedClaims.getSubject());
        assertThat(claims.getIssuedAt()).isEqualTo(expectedClaims.getIssuedAt());
        assertThat(claims.getExpiration()).isEqualTo(expectedClaims.getExpiration());
    }
}
