package com.lucas.chessapi.domain;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.security.jwt.JwtCreator;
import com.lucas.chessapi.security.jwt.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtCreatorContext {
    protected SecurityConfiguration securityConfiguration;
    protected JwtCreator jwtCreator;
    private JwtDto jwtDto;
    private String token;
    private Exception error;

    protected void given(JwtDto dto) {
        jwtDto = dto;
    }

    protected void givenValidJwtDto() {
        Date today = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS));
        Date tomorrow = Date.from(today.toInstant().plus(1, ChronoUnit.DAYS));
        jwtDto = new JwtDto("1", today, tomorrow);
    }

    protected void whenTokenIsGenerated() {
        try {
            token = jwtCreator.generateToken(jwtDto);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenParsedClaimsFieldsShouldMatch() {
        Claims claims = Jwts.parser()
                .setSigningKey(securityConfiguration.key())
                .parseClaimsJws(token).getBody();
        assertThat(claims.getSubject()).isEqualTo(jwtDto.subject());
        assertThat(claims.getIssuedAt()).isEqualTo(jwtDto.issuedAt());
        assertThat(claims.getExpiration()).isEqualTo(jwtDto.expiration());
    }

    protected void thenShouldThrow(Class<? extends Exception> exceptionClass, String expectedMessage) {
        assertThat(error)
                .isInstanceOf(exceptionClass)
                .hasMessage(expectedMessage);
    }
}
