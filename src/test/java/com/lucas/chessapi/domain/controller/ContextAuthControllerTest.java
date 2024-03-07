package com.lucas.chessapi.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ContextAuthControllerTest extends TestContextHelper {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PasswordEncoder encoder;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private UserRepository repository;

    private ResultActions result;

    protected void givenUserExists(UserEntity user) {
        repository.save(user);
    }

    protected void whenAuthenticationIsMadeFor(AuthRequestDto request) {
        try {
            result = mockMvc.perform(post("/chessapi/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)));
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenIsExpectedFromResponse(ResultMatcher... resultMatchers) throws Exception {
        result.andExpectAll(resultMatchers);
    }
}
