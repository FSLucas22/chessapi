package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.service.ContextCreateUserServiceTest;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import com.lucas.chessapi.exceptions.UserCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.Utils.generateRandomString;

public class CreateUserServiceTest extends ContextCreateUserServiceTest {
    private UserCreationRequestDto request;

    @BeforeEach
    void setUp() {
        request = new UserCreationRequestDto(
                "testuser",
                "test@email.com",
                "test123"
        );
    }

    @Test
    void shouldCreateValidUser() {
        givenUserDontExistFor("test@email.com");
        givenRepositoryReturnsOnSave(
                UserEntityBuilderExtension
                        .valid()
                        .id(1L)
                        .username("testuser")
                        .email("test@email.com")
                        .build()
        );
        givenEncodedPasswordIs(generateRandomString());
        whenUserIsCreated(request);
        thenShouldHaveNoErrors();
        thenPasswordMustHaveBeenEncoded("test123");
        thenResponseShouldBe(new UserCreationResponseDto(
                1L,
                "testuser",
                "test@email.com"
        ));
    }

    @Test
    void shouldThrowUserCreationExceptionWhenEmailAlreadyExists() {
        givenUserExistsFor("test@email.com");
        whenUserIsCreated(request);
        thenShouldThrow(UserCreationException.class, "Email already exists");
        thenShouldNotEncodePassword();
        thenShouldNotSaveUser();
    }
}
