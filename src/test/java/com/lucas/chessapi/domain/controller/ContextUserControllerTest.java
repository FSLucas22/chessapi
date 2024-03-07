package com.lucas.chessapi.domain.controller;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.ControllerContextHelper;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


public class ContextUserControllerTest extends ControllerContextHelper {

    @Autowired
    private UserRepository repository;

    protected void givenEmailAlreadyExistsFor(String email) {
        var user = UserEntityBuilderExtension.valid().id(null).email(email).build();
        repository.save(user);
    }

    protected void whenUserCreationRequestIsSend(
            UserCreationRequestDto requestContent
    ) {
        whenRequestIsPerformed(post("/chessapi/user")
                        .contentType(MediaType.APPLICATION_JSON),
                requestContent
        );
    }

    protected void thenHeaderLocationFollowedByIdShouldBe(String location) throws Exception {
        var responseBody = result.andReturn()
                .getResponse()
                .getContentAsString();

        var response = mapper.readValue(responseBody, UserCreationResponseDto.class);
        var id = response.id();

        result.andExpect(header().string("location", location + id));
    }
}
