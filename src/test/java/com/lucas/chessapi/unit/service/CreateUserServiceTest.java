package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.service.ContextCreateUserServiceTest;
import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;
import com.lucas.chessapi.exceptions.UserCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.Utils.generateRandomString;

public class CreateUserServiceTest extends ContextCreateUserServiceTest {
    private CreateUserRequestDto request;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequestDto(
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
        thenResponseShouldBe(new CreateUserResponseDto(
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
