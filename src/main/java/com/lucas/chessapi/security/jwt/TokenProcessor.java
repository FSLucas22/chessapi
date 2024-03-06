package com.lucas.chessapi.security.jwt;

import com.lucas.chessapi.builders.DateFactory;
import com.lucas.chessapi.configuration.SecurityConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class TokenProcessor {
    private final SecurityConfiguration securityConfiguration;

    public JwtTokenDto getJwtTokenDtoFromToken(String token) {
        var claims = Jwts.parser()
                .setSigningKey(securityConfiguration.getKey())
                .parseClaimsJws(token).getBody();
        return JwtTokenDto.fromClaims(claims);
    }

    public String issueToken(String subject, Date issueDate) {
        var expiration = DateFactory.expirationDate(issueDate, securityConfiguration.getExpiration());
        return generateToken(new JwtTokenDto(subject, DateFactory.today(), expiration));
    }

    private String generateToken(JwtTokenDto dto) {
        return Jwts.builder()
                .setClaims(dto.toClaims())
                .signWith(SignatureAlgorithm.HS512, securityConfiguration.getKey())
                .compact();
    }
}
