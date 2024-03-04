package com.lucas.chessapi.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public record JwtDto(String subject, Date issuedAt, Date expiration) {
    public Claims toClaims() {
        return Jwts.claims()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration);
    }

    public static JwtDto fromClaims(Claims claims) {
        return new JwtDto(claims.getSubject(), claims.getIssuedAt(), claims.getExpiration());
    }
}
