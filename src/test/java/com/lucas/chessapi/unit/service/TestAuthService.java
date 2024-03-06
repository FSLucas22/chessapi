package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.service.TestAuthServiceContext;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;
import com.lucas.chessapi.exceptions.UserAuthenticationException;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAuthService extends TestAuthServiceContext {
    private AuthRequestDto request;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        request = new AuthRequestDto("test@email.com", "test123");
        user = UserEntityBuilder
                .getBuilder()
                .id(1L)
                .email("test@email.com")
                .password("test123")
                .username("testuser")
                .build();
    }

    @Test
    void shouldAuthenticateUserWithCorrectRequest() {
        givenUserExists(user);
        givenReturnedTokenIs("1234");
        whenAuthenticationHappensFor(request);
        thenShouldConsultReposityWithEmail("test@email.com");
        thenShouldIssueToken();
        thenResponseShouldBe(new AuthResponseDto("testuser", "1234"));
    }

    @Test
    void shouldThrowUserAuthenticationExceptionWhenUserIsNotFound() {
        givenUserRepositoryReturnsEmpty();
        whenAuthenticationHappensFor(request);
        thenShouldConsultReposityWithEmail("test@email.com");
        thenShouldThrow(UserAuthenticationException.class, "Invalid email or password");
        thenShouldNotIssueToken();
    }

    @Test
    void shouldThrowUserAuthenticationExceptionWhenPasswordIsIncorrect() {
        request = new AuthRequestDto("test@email.com", "1234");
        givenUserExists(user);
        whenAuthenticationHappensFor(request);
        thenShouldConsultReposityWithEmail("test@email.com");
        thenShouldThrow(UserAuthenticationException.class, "Invalid email or password");
        thenShouldNotIssueToken();
    }
}
