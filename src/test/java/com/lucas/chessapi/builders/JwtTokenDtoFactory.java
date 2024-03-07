package com.lucas.chessapi.builders;

import com.lucas.chessapi.security.jwt.JwtTokenDto;

import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtTokenDtoFactory {

    public static JwtTokenDto jwtTokenDtoWithSubject(String subject) {
        return new JwtTokenDto(subject, DateFactory.today(), tomorrow());
    }

    private static Date tomorrow() {
        return Date.from(DateFactory.today().toInstant().plus(1, ChronoUnit.DAYS));
    }
}
