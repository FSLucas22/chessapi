package com.lucas.chessapi.security.jwt;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtCreator {
    private final SecurityConfiguration securityConfiguration;

    public String generateToken(JwtDto dto) {
        return Jwts.builder()
                .setSubject(dto.subject())
                .setIssuedAt(dto.issuedAt())
                .setExpiration(dto.expiration())
                .signWith(SignatureAlgorithm.HS512, securityConfiguration.key())
                .compact();
    }
}
