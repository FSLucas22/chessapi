package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.service.ContextAuthServiceTest;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;
import com.lucas.chessapi.exceptions.UserAuthenticationException;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.Utils.generateRandomString;

public class AuthServiceTest extends ContextAuthServiceTest {
    private AuthRequestDto request;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        request = new AuthRequestDto("test@email.com", "test123");
        var encryptedPassword = generateRandomString();

        user = UserEntityBuilderExtension
                .getBuilder()
                .id(1L)
                .email("test@email.com")
                .password(encryptedPassword)
                .username("testuser")
                .build();
    }

    @Test
    void shouldAuthenticateUserWithCorrectRequest() {
        givenUserExists(user);
        givenReturnedTokenIs("1234");
        givenEncodedPasswordMatches("test123", user.getPassword());
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
        request = new AuthRequestDto("test@email.com", "test123");
        givenUserExists(user);
        givenEncodedPasswordDontMatch("test123", user.getPassword());
        whenAuthenticationHappensFor(request);
        thenShouldConsultReposityWithEmail("test@email.com");
        thenShouldThrow(UserAuthenticationException.class, "Invalid email or password");
        thenShouldNotIssueToken();
    }
}
