package com.lucas.chessapi.integration.controller;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.ControllerContextHelper;
import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static com.lucas.chessapi.builders.DateFactory.today;
import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static com.lucas.chessapi.builders.UserEntityBuilderExtension.validUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserControllerTest extends ControllerContextHelper {
    @Autowired
    UserRepository repository;

    @Value("${security.config.prefix}")
    String prefix;

    @Autowired
    TokenProcessor tokenProcessor;

    @Test
    void shouldReturnStatus201WhenRequestIsValid() throws Exception {
        var request = new CreateUserRequestDto(
                "testuser",
                "test@email.com",
                "test123"
        );

        var performResult = performCreateUserRequestFor(request);

        var responseBody = performResult.andReturn()
                .getResponse()
                .getContentAsString();

        var response = mapper.readValue(responseBody, CreateUserResponseDto.class);
        var id = response.id();

        performResult.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", notNullValue()),
                jsonPath("$.username", equalTo("testuser")),
                jsonPath("$.email", equalTo("test@email.com")),
                header().string("location", "/chessapi/user/" + id));
    }

    @Test
    void shouldReturnStatus400WhenRequestFieldsAreBlank() throws Exception {
        var request = new CreateUserRequestDto(" ", " ", " ");

        var perfomResult = performCreateUserRequestFor(request);

        perfomResult.andExpectAll(
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

        var performResult = performCreateUserRequestFor(request);

        performResult.andExpectAll(
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
        var user = UserEntityBuilderExtension.valid().id(null).email(request.email()).build();

        repository.save(user);

        var performResult = performCreateUserRequestFor(request);

        performResult.andExpectAll(
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
        var header = generateAuthenticationFor("test@email.com");
        given(userToCreate);

        var performResult = performGetUserRequestFor(userToCreate.getId(), header);

        assertThat(header).isNotNull();
        performResult.andExpectAll(
                status().isOk(),
                jsonPath("$.id", equalTo(userToCreate.getId().intValue())),
                jsonPath("$.username", equalTo(userToCreate.getUsername())),
                jsonPath("$.email", equalTo(userToCreate.getEmail()))
        );
    }

    @Test
    void shouldReturnStatus404WhenUserDontExist() throws Exception {
        var header = generateAuthenticationFor("test@email.com");

        var performResult = performGetUserRequestFor(-1L, header);

        performResult.andExpectAll(
                status().isNotFound(),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0]", equalTo("User not found"))
        );
    }

    @Test
    void shouldReturnStatus403WhenUserIsNotAuthenticatedInGetUser() throws Exception {
        var performResult = performGetUserRequestFor(1L, "");

        performResult.andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnStatus403WhenUserIsNotAuthenticatedInGetAllUsers() throws Exception {
        var request = new GetAllUsersRequestDto(0, 3, List.of("username"));
        var performResult = performGetAllUsersRequestFor(request, "");

        performResult.andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnAllUsersInPageWithStatus200WhenUserIsAuthenticated() throws Exception {
        var request = new GetAllUsersRequestDto(0, 3, List.of("username"));
        var alice = user(null, "Alice");
        var bob = user(null, "Bob");
        var cheryl = user(null, "Cheryl");

        var header = generateAuthenticationFor("test@email.com");

        given(cheryl, bob, alice);

        var performResult = performGetAllUsersRequestFor(request, header);

        performResult
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.users", hasSize(3)))
                .andExpectAll(userInIndexMatches(0, alice))
                .andExpectAll(userInIndexMatches(1, bob))
                .andExpectAll(userInIndexMatches(2, cheryl));
    }

    ResultMatcher[] userInIndexMatches(Integer index, UserEntity user) {
        var field = String.format("$.users[%d]", index);
        return new ResultMatcher[]{
                jsonPath(field + ".id", equalTo(user.getId().intValue())),
                jsonPath(field + ".username", equalTo(user.getUsername())),
                jsonPath(field + ".email", equalTo(user.getEmail()))
        };
    }

    String generateAuthenticationFor(String email) {
        var user = UserEntityBuilderExtension
                .valid()
                .id(null)
                .email(email)
                .build();

        repository.save(user);
        var token = tokenProcessor.issueToken(user.getId().toString(), today());
        return prefix + " " + token;
    }

    void given(UserEntity... users) {
        Arrays.stream(users).forEach(repository::save);
    }

    ResultActions performCreateUserRequestFor(CreateUserRequestDto request) throws Exception {
        return mockMvc.perform(post("/chessapi/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    ResultActions performGetUserRequestFor(Long id, String header) throws Exception {
        return mockMvc.perform(get("/chessapi/user/" + id)
                .header("Authorization", header));
    }

    ResultActions performGetAllUsersRequestFor(GetAllUsersRequestDto request, String header)
            throws Exception {
        return mockMvc.perform(get("/chessapi/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("Authorization", header));
    }
}
