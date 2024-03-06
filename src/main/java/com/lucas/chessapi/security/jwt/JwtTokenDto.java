package com.lucas.chessapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public record JwtTokenDto(String subject, Date issuedAt, Date expiration) {
    public Claims toClaims() {
        return Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration);
    }

    public static JwtTokenDto fromClaims(Claims claims) {
        return new JwtTokenDto(claims.getSubject(), claims.getIssuedAt(), claims.getExpiration());
    }
}
