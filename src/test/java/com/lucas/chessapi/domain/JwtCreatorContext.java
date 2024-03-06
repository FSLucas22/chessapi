package com.lucas.chessapi.domain;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.security.jwt.JwtCreator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtCreatorContext extends TestContextHelper{
    protected SecurityConfiguration securityConfiguration;
    protected JwtCreator jwtCreator;
    private String token;
    private String subject;

    protected void givenSubject(String subject) {
        this.subject = subject;
    }

    protected void whenIssueTokenIsCalled(Date forIssueDate) {
        token = jwtCreator.issueToken(subject, forIssueDate);
    }

    protected void thenParsedClaimsFieldsShouldMatch(Claims expectedClaims) {
        Claims claims = Jwts.parser()
                .setSigningKey(securityConfiguration.key())
                .parseClaimsJws(token).getBody();
        assertThat(claims.getSubject()).isEqualTo(expectedClaims.getSubject());
        assertThat(claims.getIssuedAt()).isEqualTo(expectedClaims.getIssuedAt());
        assertThat(claims.getExpiration()).isEqualTo(expectedClaims.getExpiration());
    }

    protected void thenShouldRecoverDataFromToken() {
        var dto = jwtCreator.getJwtDtoFromToken(token);
        thenParsedClaimsFieldsShouldMatch(dto.toClaims());
    }
}
