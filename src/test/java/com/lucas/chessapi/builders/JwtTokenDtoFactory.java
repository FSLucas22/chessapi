package com.lucas.chessapi.builders;

import com.lucas.chessapi.security.jwt.JwtTokenDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtTokenDtoFactory {
    private final static Date today = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS));
    private final static Date tomorrow = Date.from(today.toInstant().plus(1, ChronoUnit.DAYS));

    public static JwtTokenDto jwtTokenDtoWithSubject(String subject) {
        return new JwtTokenDto(subject, today, tomorrow);
    }
}
