package com.lucas.chessapi.security.jwt;

import com.lucas.chessapi.builders.DateFactory;
import com.lucas.chessapi.configuration.SecurityConfiguration;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@RequiredArgsConstructor
public class JwtCreator {
    private final SecurityConfiguration securityConfiguration;

    public JwtDto getJwtDtoFromToken(String token) {
        var claims = Jwts.parser()
                .setSigningKey(securityConfiguration.key())
                .parseClaimsJws(token).getBody();
        return JwtDto.fromClaims(claims);
    }

    public String issueToken(String subject, Date issueDate) {
        var expiration = DateFactory.expirationDate(issueDate, securityConfiguration.expiration());
        return generateToken(new JwtDto(subject, DateFactory.today(), expiration));
    }

    private String generateToken(JwtDto dto) {
        return Jwts.builder()
                .setClaims(dto.toClaims())
                .signWith(SignatureAlgorithm.HS512, securityConfiguration.key())
                .compact();
    }
}
