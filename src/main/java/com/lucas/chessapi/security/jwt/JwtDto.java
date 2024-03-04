package com.lucas.chessapi.security.jwt;

import java.util.Date;

public record JwtDto(String subject, Date issuedAt, Date expiration){}
