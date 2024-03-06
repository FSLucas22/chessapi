package com.lucas.chessapi.domain.security;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenProcessorContext extends TestContextHelper {
    protected SecurityConfiguration securityConfiguration;
    protected TokenProcessor tokenProcessor;
    private String token;
    private String subject;

    protected void givenSubject(String subject) {
        this.subject = subject;
    }

    protected void whenIssueTokenIsCalled(Date forIssueDate) {
        token = tokenProcessor.issueToken(subject, forIssueDate);
    }

    protected void thenShouldRecoverDataFromToken() {
        var dto = tokenProcessor.getJwtTokenDtoFromToken(token);
        thenParsedClaimsFieldsShouldMatch(dto.toClaims());
    }

    protected void thenParsedClaimsFieldsShouldMatch(Claims expectedClaims) {
        Claims claims = Jwts.parser()
                .setSigningKey(securityConfiguration.getKey())
                .parseClaimsJws(token).getBody();
        assertThat(claims.getSubject()).isEqualTo(expectedClaims.getSubject());
        assertThat(claims.getIssuedAt()).isEqualTo(expectedClaims.getIssuedAt());
        assertThat(claims.getExpiration()).isEqualTo(expectedClaims.getExpiration());
    }
}
