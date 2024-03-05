package com.lucas.chessapi.builders;

import com.lucas.chessapi.security.jwt.JwtDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtDtoFactory {
    private final static Date today = Date.from(Instant.now().truncatedTo(ChronoUnit.DAYS));
    private final static Date tomorrow = Date.from(today.toInstant().plus(1, ChronoUnit.DAYS));

    public static JwtDto jwtDtoWithSubject(String subject) {
        return new JwtDto(subject, today, tomorrow);
    }
}
