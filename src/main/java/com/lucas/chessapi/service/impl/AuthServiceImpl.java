package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.builders.DateFactory;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;
import com.lucas.chessapi.exceptions.UserAuthenticationException;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import com.lucas.chessapi.service.AuthService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final TokenProcessor tokenProcessor;

    @Override
    public AuthResponseDto authenticate(AuthRequestDto request) {
        var user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UserAuthenticationException("Invalid email or password"));
        validatePassword(user.getPassword(), request.password());
        var token = tokenProcessor.issueToken(user.getId().toString(), DateFactory.today());
        return new AuthResponseDto(user.getUsername(), token);
    }

    private void validatePassword(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new UserAuthenticationException("Invalid email or password");
        }
    }
}
