package com.lucas.chessapi.domain.controller;

import com.lucas.chessapi.domain.ControllerContextHelper;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class ContextAuthControllerTest extends ControllerContextHelper {
    @Autowired
    private UserRepository repository;

    protected void givenUserExists(UserEntity user) {
        repository.save(user);
    }

    protected void whenAuthenticationIsMadeFor(AuthRequestDto request) {
        whenRequestIsPerformed(post("/chessapi/auth")
                .contentType(MediaType.APPLICATION_JSON), request);
    }
}
