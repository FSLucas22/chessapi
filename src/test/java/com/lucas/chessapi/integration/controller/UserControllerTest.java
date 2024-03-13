package com.lucas.chessapi.integration.controller;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.controller.ContextUserControllerTest;
import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static com.lucas.chessapi.builders.UserEntityBuilderExtension.validUserEntity;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends ContextUserControllerTest {

    @Test
    void shouldReturnStatus201WhenRequestIsValid() throws Exception {
        var request = new CreateUserRequestDto(
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
        var request = new CreateUserRequestDto(" ", " ", " ");
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
        var request = new CreateUserRequestDto(null, null, null);
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
        var request = CreateUserRequestDto.fromUserEntity(validUserEntity());

        givenEmailAlreadyExistsFor(request.email());

        whenUserCreationRequestIsSend(request);

        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isConflict(),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0]", equalTo("Email already exists"))
        );
    }

    @Test
    void shouldReturnStatus200WhenGetUserIsCalledOnExistingUser() throws Exception {
        var userToCreate = UserEntityBuilderExtension
                .valid()
                .id(null)
                .email("test2@email.com")
                .build();
        givenAuthenticationHappensFor("test@email.com");
        givenUser(userToCreate);
        whenGetUserRequestIsSendFor(userToCreate.getId());
        thenShouldHaveNoErrors();
        thenHeaderAuthorizationMustExist();
        thenIsExpectedFromResponse(
                status().isOk(),
                jsonPath("$.id", equalTo(userToCreate.getId().intValue())),
                jsonPath("$.username", equalTo(userToCreate.getUsername())),
                jsonPath("$.email", equalTo(userToCreate.getEmail()))
        );
    }

    @Test
    void shouldReturnStatus404WhenUserDontExist() throws Exception {
        givenAuthenticationHappensFor("test@email.com");
        whenGetUserRequestIsSendFor(-1L);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isNotFound(),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0]", equalTo("User not found"))
        );
    }

    @Test
    void shouldReturnStatus403WhenUserIsNotAuthenticatedInGetUser() throws Exception {
        whenGetUserRequestIsSendFor(1L);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isForbidden());
    }

    @Test
    void shouldNotKeepSessionForAuthentication() throws Exception {
        givenAuthenticationHappensFor("test@email.com");
        whenHeaderIsCleaned();
        whenGetUserRequestIsSendFor(1L);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isForbidden());
    }

    @Test
    void shouldReturnStatus403WhenUserIsNotAuthenticatedInGetAllUsers() throws Exception {
        var request = new GetAllUsersRequestDto(0, 3, List.of("username"));
        whenGetAllUsersRequestIsSendWith(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isForbidden());
    }

    @Test
    void shouldReturnAllUsersInPageWithStatus200WhenUserIsAuthenticated() throws Exception {
        var request = new GetAllUsersRequestDto(0, 3, List.of("username"));
        var alice = user(null, "Alice");
        var bob = user(null, "Bob");
        var cheryl = user(null, "Cheryl");

        givenAuthenticationHappensFor("test@email.com");
        givenUsers(cheryl, bob, alice);
        whenGetAllUsersRequestIsSendWith(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isOk(),
                jsonPath("$.users", hasSize(3))
        );
        thenIsExpectedFromResponse(userInIndexMatches(0, alice));
        thenIsExpectedFromResponse(userInIndexMatches(1, bob));
        thenIsExpectedFromResponse(userInIndexMatches(2, cheryl));
    }

    private ResultMatcher[] userInIndexMatches(Integer index, UserEntity user) {
        var field = String.format("$.users[%d]", index);
        return new ResultMatcher[]{
                jsonPath(field + ".id", equalTo(user.getId().intValue())),
                jsonPath(field + ".username", equalTo(user.getUsername())),
                jsonPath(field + ".email", equalTo(user.getEmail()))
        };
    }
}
