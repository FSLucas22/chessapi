package com.lucas.chessapi.integration.controller;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.domain.ControllerContextHelper;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.lucas.chessapi.builders.DateFactory.today;
import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GameControllerTest extends ControllerContextHelper {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    TokenProcessor tokenProcessor;

    @Value("${security.config.prefix}")
    String prefix;

    UserEntity alice;
    UserEntity bob;

    GameEntity game1;
    GameEntity game2;

    @BeforeEach
    void setUp() {
        alice = user(null, "Alice");
        bob = user(null, "Bob");

        given(alice, bob);

        var gameBuilder = GameEntityBuilder.valid().id(null);
        game1 = gameBuilder.firstPlayer(alice).secondPlayer(bob).build();
        game2 = gameBuilder.firstPlayer(bob).secondPlayer(alice).build();

        given(game1, game2);
    }

    @Test
    void shouldReturnStatus200WhenGetGameRequestIsValid() throws Exception {
        var header = generateAuthenticationFor(alice);
        performGetGameRequestIsPerformedFor(game1.getId(), header)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(game1.getId().intValue())),
                        jsonPath("$.firstPlayer.id", equalTo(alice.getId().intValue())),
                        jsonPath("$.firstPlayer.username", equalTo(alice.getUsername())),
                        jsonPath("$.secondPlayer.id", equalTo(bob.getId().intValue())),
                        jsonPath("$.secondPlayer.username", equalTo(bob.getUsername()))
                );
    }

    @Test
    void shouldReturnStatus403WhenRequestIsPerformedWithoutAuthentication() throws Exception {
        performGetGameRequestIsPerformedFor(1L, "").andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnStatus404WhenGameDontExist() throws Exception {
        var header = generateAuthenticationFor(alice);
        performGetGameRequestIsPerformedFor(-1L, header).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnStatus200WhenGetAllUserGamesIsCalledAndUserIsAuthenticated() throws Exception {
        var header = generateAuthenticationFor(alice);
        performGetAllUserGamesIsRequestPerformedWithParams(alice.getId(), 0, 3, header)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.player.id", equalTo(alice.getId().intValue())),
                        jsonPath("$.player.username", equalTo(alice.getUsername())),
                        jsonPath("$.games", hasSize(2)),
                        jsonPath("$.hasNext", equalTo(false))
                )
                .andExpectAll(gameInIndexMatches(0, game1))
                .andExpectAll(gameInIndexMatches(1, game2));
    }

    @Test
    void shouldReturnStatus409WhenGetAllUserGamesIsCalledAndUserDontExist() throws Exception {
        var header = generateAuthenticationFor(alice);
        performGetAllUserGamesIsRequestPerformedWithParams(-1L, 0, 3, header)
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnStatus403WhenGetAllUserGamesIsCalledAndUserIsNotAuthenticated() throws Exception {
        performGetAllUserGamesIsRequestPerformedWithParams(1L, 0, 3, "")
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnStatus201WhenCreateGameRequestIsCalledWithAuthenticatedUser() throws Exception {
        var request = new CreateGameRequestDto(bob.getId(), OrderedPairCreationType.AS_PASSED);
        var header = generateAuthenticationFor(alice);
        var performResult = performCreateGameRequestFor(request, header);

        var responseBody = performResult.andReturn().getResponse().getContentAsString();
        var response = mapper.readValue(responseBody, CreateGameResponseDto.class);
        var id = response.id();

        performResult.andExpectAll(
                status().isCreated(),
                jsonPath("$.id", notNullValue()),
                jsonPath("$.firstPlayer.id", equalTo(alice.getId().intValue())),
                jsonPath("$.firstPlayer.username", equalTo(alice.getUsername())),
                jsonPath("$.secondPlayer.id", equalTo(bob.getId().intValue())),
                jsonPath("$.secondPlayer.username", equalTo(bob.getUsername())),
                header().string("location", "/chessapi/game/" + id)
        );
    }

    @Test
    void shouldReturnStatus409WhenCreateGameRequestIsCalledWithNonExistingAdversary() throws Exception {
        var request = new CreateGameRequestDto(-1L, OrderedPairCreationType.AS_PASSED);
        var header = generateAuthenticationFor(alice);
        performCreateGameRequestFor(request, header).andExpectAll(
                status().isConflict(),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0]", equalTo("Player not found"))
        );
    }

    @Test
    void shouldReturnStatus400WhenCreateGameRequestIsCalledWithInvalidRequest() throws Exception {
        var request = new CreateGameRequestDto(null, null);
        var header = generateAuthenticationFor(alice);
        performCreateGameRequestFor(request, header).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errors", hasSize(2)),
                jsonPath("$.errors", containsInAnyOrder(
                        "adversaryId must not be null",
                        "strategy must not be null"
                ))
        );
    }

    @Test
    void shouldReturnStatus403WhenCreateGameIsCalledWithoutAuthentication() throws Exception {
        var request = new CreateGameRequestDto(1L, OrderedPairCreationType.AS_PASSED);
        performCreateGameRequestFor(request, "").andExpect(status().isForbidden());
    }

    @Test
    void shouldExpireGameWhenFirstPlayerDontMakeFirstMoveInTime() throws Exception {
        var header = generateAuthenticationFor(alice);
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(alice)
                .secondPlayer(bob)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .numberOfMoves(0)
                .firstPlayerRemainingTimeMillis(0L)
                .build();
        given(game);

        performGetGameRequestIsPerformedFor(game.getId(), header)
                .andExpect(jsonPath("$.status", equalTo(GameStatus.EXPIRED.toString())));
    }

    @Test
    void shouldExpireGameWhenSecondPlayerDontMakeSecondMoveInTime() throws Exception {
        var header = generateAuthenticationFor(alice);
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(alice)
                .secondPlayer(bob)
                .status(GameStatus.WAITING_SECOND_PLAYER)
                .numberOfMoves(1)
                .secondPlayerRemainingTimeMillis(0L)
                .build();

        given(game);

        performGetGameRequestIsPerformedFor(game.getId(), header)
                .andExpect(jsonPath("$.status", equalTo(GameStatus.EXPIRED.toString())));
    }

    @Test
    void shouldGiveFirstPlayerWinWhenSecondPlayerLoseOnTime() throws Exception {
        var header = generateAuthenticationFor(alice);
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(alice)
                .secondPlayer(bob)
                .status(GameStatus.WAITING_SECOND_PLAYER)
                .numberOfMoves(4)
                .secondPlayerRemainingTimeMillis(0L)
                .build();

        given(game);

        performGetGameRequestIsPerformedFor(game.getId(), header)
                .andExpect(jsonPath("$.status", equalTo(GameStatus.WON_BY_FIRST_PLAYER.toString())));
    }

    @Test
    void shouldGiveSecondPlayerWinWhenFirstPlayerLoseOnTime() throws Exception {
        var header = generateAuthenticationFor(alice);
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(alice)
                .secondPlayer(bob)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .numberOfMoves(3)
                .firstPlayerRemainingTimeMillis(0L)
                .build();

        given(game);

        performGetGameRequestIsPerformedFor(game.getId(), header)
                .andExpect(jsonPath("$.status", equalTo(GameStatus.WON_BY_SECOND_PLAYER.toString())));
    }


    ResultMatcher[] gameInIndexMatches(Integer index, GameEntity game) {
        var field = String.format("$.games[%d]", index);
        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return new ResultMatcher[]{
                jsonPath(field + ".id", equalTo(game.getId().intValue())),
                jsonPath(field + ".firstPlayer.id", equalTo(firstPlayer.getId().intValue())),
                jsonPath(field + ".firstPlayer.username", equalTo(firstPlayer.getUsername())),
                jsonPath(field + ".secondPlayer.id", equalTo(secondPlayer.getId().intValue())),
                jsonPath(field + ".secondPlayer.username", equalTo(secondPlayer.getUsername())),
                jsonPath(field + ".createdAt", equalTo(game.getCreatedAt().format(formatter))),
                jsonPath(field + ".updatedAt", equalTo(game.getUpdatedAt().format(formatter)))
        };
    }

    void given(UserEntity... entities) {
        userRepository.deleteAll();
        Arrays.stream(entities).forEach(userRepository::save);
    }

    void given(GameEntity... entities) {
        Arrays.stream(entities).forEach(gameRepository::save);
    }

    ResultActions performGetGameRequestIsPerformedFor(Long id, String header) throws Exception {
        return mockMvc.perform(get("/chessapi/game/" + id)
                .header("Authorization", header));
    }

    ResultActions performGetAllUserGamesIsRequestPerformedWithParams(
            Long id, Integer pageNumber, Integer pageSize, String header
    ) throws Exception {
        return mockMvc.perform(
                get(String.format("/chessapi/game/user/%d?page=%d&limit=%d", id, pageNumber, pageSize))
                        .header("Authorization", header));
    }

    ResultActions performCreateGameRequestFor(CreateGameRequestDto request, String header)
            throws Exception {
        return mockMvc.perform(post("/chessapi/game")
                .header("Authorization", header)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)));
    }

    String generateAuthenticationFor(UserEntity user) {
        var token = tokenProcessor.issueToken(user.getId().toString(), today());
        return prefix + " " + token;
    }
}
