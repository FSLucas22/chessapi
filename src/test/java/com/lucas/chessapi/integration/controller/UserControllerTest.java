package com.lucas.chessapi.integration.controller;

import com.lucas.chessapi.domain.controller.ContextUserControllerTest;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.validUserEntity;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ContextUserControllerTest {

    @Test
    void shouldReturnStatus201WhenRequestIsValid() throws Exception {
        var request = new UserCreationRequestDto(
                "testuser",
                "test@email.com",
                "test123"
        );

        whenUserCreationRequestIsSend(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isCreated(),
                jsonPath("$.id", notNullValue()),
                jsonPath("$.username", equalTo("testuser")),
                jsonPath("$.email", equalTo("test@email.com"))
        );
        thenHeaderLocationFollowedByIdShouldBe("/chessapi/user/");
    }

    @Test
    void shouldReturnStatus400WhenRequestFieldsAreBlank() throws Exception {
        var request = new UserCreationRequestDto(" ", " ", " ");
        whenUserCreationRequestIsSend(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isBadRequest(),
                jsonPath("$.errors", hasSize(3)),
                jsonPath("$.errors", containsInAnyOrder(
                        "username must not be blank",
                        "email must not be blank",
                        "password must not be blank"
                ))
        );
    }

    @Test
    void shouldReturnStatus400WhenRequestFieldsAreNull() throws Exception {
        var request = new UserCreationRequestDto(null, null, null);
        whenUserCreationRequestIsSend(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isBadRequest(),
                jsonPath("$.errors", hasSize(3)),
                jsonPath("$.errors", containsInAnyOrder(
                        "username must not be blank",
                        "email must not be blank",
                        "password must not be blank"
                ))
        );
    }

    @Test
    void shouldReturnStatus409WhenEmailAlreadyExists() throws Exception {
        var request = UserCreationRequestDto.fromUserEntity(validUserEntity());

        givenEmailAlreadyExistsFor(request.email());

        whenUserCreationRequestIsSend(request);

        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isConflict(),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0]", equalTo("Email already exists"))
        );
    }
}
