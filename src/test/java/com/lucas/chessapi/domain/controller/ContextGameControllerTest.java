package com.lucas.chessapi.domain.controller;

import com.lucas.chessapi.domain.ControllerContextHelper;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

public class ContextGameControllerTest extends ControllerContextHelper {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    protected void given(UserEntity... entities) {
        userRepository.deleteAll();
        Arrays.stream(entities).forEach(userRepository::save);
    }

    protected void given(GameEntity... entities) {
        Arrays.stream(entities).forEach(gameRepository::save);
    }

    protected void whenGetGameRequestIsPerformedForId(Long id) {
        whenRequestIsPerformed(
                get("/chessapi/game/" + id)
                        .header("Authorization", authContext.getHeader())
        );
    }

    protected void whenGetAllUserGamesIsRequestPerformedWithParams(Long id, Integer pageNumber, Integer pageSize) {
        whenRequestIsPerformed(
                get(String.format("/chessapi/game/user/%d?page=%d&limit=%d", id, pageNumber, pageSize))
                        .header("Authorization", authContext.getHeader())
        );
    }

    protected void whenCreateGameRequestIsPerformedWith(CreateGameRequestDto request) {
        whenRequestIsPerformed(
                post("/chessapi/game")
                        .header("Authorization", authContext.getHeader())
                        .contentType(MediaType.APPLICATION_JSON),
                request
        );
    }

    protected void thenHeaderLocationFollowedByIdShouldBe(String location)
            throws Exception {
        var responseBody = result.andReturn().getResponse().getContentAsString();
        var response = mapper.readValue(responseBody, CreateGameResponseDto.class);
        var id = response.id();
        result.andExpect(header().string("location", location + id));
    }
}
