package com.lucas.chessapi.integration.controller;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.controller.ContextAuthControllerTest;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.lucas.chessapi.Utils.generateRandomString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ContextAuthControllerTest {
    @Autowired
    private PasswordEncoder encoder;

    private UserEntity user;
    private String unencodedPassword;

    @BeforeEach
    void setUp() {
        unencodedPassword = generateRandomString();
        var encryptedPassword = encoder.encode(unencodedPassword);

        user = UserEntityBuilderExtension
                .getBuilder()
                .createdNow()
                .id(null)
                .username("testuser")
                .email("user@test.com")
                .password(encryptedPassword)
                .build();
    }

    @Test
    void shouldReturnStatusOkWhenRequestIsCorrect() throws Exception {
        var request = new AuthRequestDto("user@test.com", unencodedPassword);

        givenUserExists(user);
        whenAuthenticationIsMadeFor(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isOk(),
                jsonPath("$.username", equalTo("testuser")),
                jsonPath("$.token", notNullValue())
        );
    }

    @Test
    void shouldReturnStatusBadRequestWhenUserIsNotFound() throws Exception {
        var request = new AuthRequestDto("user@test.com", unencodedPassword);
        whenAuthenticationIsMadeFor(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isBadRequest(),
                jsonPath("$.message", equalTo("Invalid email or password")),
                jsonPath("$.statusCode", equalTo(400))
        );
    }

    @Test
    void shouldReturnStatusBadRequestWhenPasswordIsIncorrect() throws Exception {
        var request = new AuthRequestDto("user@test.com", "xyz");
        whenAuthenticationIsMadeFor(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isBadRequest(),
                jsonPath("$.message", equalTo("Invalid email or password")),
                jsonPath("$.statusCode", equalTo(400))
        );
    }
}
