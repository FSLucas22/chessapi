package com.lucas.chessapi.unit;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.security.jwt.JwtCreator;
import com.lucas.chessapi.security.jwt.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtCreatorTest {
    SecurityConfiguration securityConfiguration;
    JwtCreator jwtCreator;

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
        // Given
        String securityKey = securityConfiguration.key();
        Date today = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS));
        Date tomorrow = Date.from(today.toInstant().plus(1, ChronoUnit.DAYS));
        var jwtDto = new JwtDto("1", today, tomorrow);

        // When
        String token = jwtCreator.generateToken(jwtDto);

        // Then
        Claims claims = Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
        assertThat(claims.getSubject()).isEqualTo(jwtDto.subject());
        assertThat(claims.getIssuedAt()).isEqualTo(jwtDto.issuedAt());
        assertThat(claims.getExpiration()).isEqualTo(jwtDto.expiration());
    }
}
