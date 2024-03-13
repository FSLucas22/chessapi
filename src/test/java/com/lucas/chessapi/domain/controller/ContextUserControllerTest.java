package com.lucas.chessapi.domain.controller;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.ControllerContextHelper;
import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static com.lucas.chessapi.builders.DateFactory.today;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


public class ContextUserControllerTest extends ControllerContextHelper {

    @Autowired
    private UserRepository repository;

    @Value("${security.config.prefix}")
    private String prefix;

    @Autowired
    private TokenProcessor tokenProcessor;

    private String header = "";
    private String token;

    protected void givenEmailAlreadyExistsFor(String email) {
        var user = UserEntityBuilderExtension.valid().id(null).email(email).build();
        repository.save(user);
    }

    protected void givenAuthenticationHappensFor(String email) {
        var user = UserEntityBuilderExtension
                .valid()
                .id(null)
                .email(email)
                .build();

        repository.save(user);
        token = tokenProcessor.issueToken(user.getId().toString(), today());
        header = prefix + " " + token;
    }

    protected void givenUser(UserEntity userToCreate) {
        repository.save(userToCreate);
    }

    protected void givenUsers(UserEntity... users) {
        Arrays.stream(users).forEach(repository::save);
    }

    protected void whenHeaderIsCleaned() {
        token = null;
        header = "";
    }

    protected void whenUserCreationRequestIsSend(
            CreateUserRequestDto requestContent
    ) {
        whenRequestIsPerformed(post("/chessapi/user")
                        .contentType(MediaType.APPLICATION_JSON),
                requestContent
        );
    }

    protected void whenGetUserRequestIsSendFor(Long id) {
        whenRequestIsPerformed(get("/chessapi/user/" + id)
                .header("Authorization", header)
        );
    }

    protected void whenGetAllUsersRequestIsSendWith(GetAllUsersRequestDto request) {
        whenRequestIsPerformed(
                get("/chessapi/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", header),
                request
        );
    }

    protected void thenHeaderLocationFollowedByIdShouldBe(String location) throws Exception {
        var responseBody = result.andReturn()
                .getResponse()
                .getContentAsString();

        var response = mapper.readValue(responseBody, CreateUserResponseDto.class);
        var id = response.id();

        result.andExpect(header().string("location", location + id));
    }

    protected void thenHeaderAuthorizationMustExist() {
        assertThat(token).isNotNull();
        assertThat(header).isNotNull();
    }
}
