package com.lucas.chessapi.security.jwt;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.exceptions.InvalidJwtDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtCreator {
    private final SecurityConfiguration securityConfiguration;

    public String generateToken(JwtDto dto) {
        validateJwtDto(dto);

        return Jwts.builder()
                .setSubject(dto.subject())
                .setIssuedAt(dto.issuedAt())
                .setExpiration(dto.expiration())
                .signWith(SignatureAlgorithm.HS512, securityConfiguration.key())
                .compact();
    }

    private void validateJwtDto(JwtDto dto) {
        if (dto.subject() == null)
            throw new InvalidJwtDto("Subject cannot be null");

        if (dto.subject().isBlank())
            throw new InvalidJwtDto("Subject cannot be blank");

        if (dto.issuedAt() == null)
            throw new InvalidJwtDto("Issue date cannot be null");

        if (dto.expiration() == null)
            throw new InvalidJwtDto("Expiration date cannot be null");
    }
}
