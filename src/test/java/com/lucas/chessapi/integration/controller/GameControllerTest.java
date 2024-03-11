package com.lucas.chessapi.integration.controller;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.domain.controller.ContextGameControllerTest;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.format.DateTimeFormatter;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest extends ContextGameControllerTest {
    private UserEntity alice;
    private UserEntity bob;

    private GameEntity game1;
    private GameEntity game2;

    @BeforeEach
    void setUp() {
        authContext.clean();
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
        givenAuthenticationHappensFor(alice);
        whenGetGameRequestIsPerformedForId(game1.getId());
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
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
        whenGetGameRequestIsPerformedForId(1L);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isForbidden());
    }

    @Test
    void shouldReturnStatus404WhenGameDontExist() throws Exception {
        givenAuthenticationHappensFor(alice);
        whenGetGameRequestIsPerformedForId(-1L);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isNotFound());
    }

    @Test
    void shouldReturnStatus200WhenGetAllUserGamesIsCalledAndUserIsAuthenticated() throws Exception {
        givenAuthenticationHappensFor(alice);
        whenGetAllUserGamesIsRequestPerformedWithParams(alice.getId(), 0, 3);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isOk(),
                jsonPath("$.player.id", equalTo(alice.getId().intValue())),
                jsonPath("$.player.username", equalTo(alice.getUsername())),
                jsonPath("$.games", hasSize(2)),
                jsonPath("$.hasNext", equalTo(false))
        );

        thenIsExpectedFromResponse(gameInIndexMatches(0, game1));
        thenIsExpectedFromResponse(gameInIndexMatches(1, game2));
    }

    @Test
    void shouldReturnStatus409WhenGetAllUserGamesIsCalledAndUserDontExist() throws Exception {
        givenAuthenticationHappensFor(alice);
        whenGetAllUserGamesIsRequestPerformedWithParams(-1L, 0, 3);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isConflict());
    }

    @Test
    void shouldReturnStatus403WhenGetAllUserGamesIsCalledAndUserIsNotAuthenticated() throws Exception {
        whenGetAllUserGamesIsRequestPerformedWithParams(1L, 0, 3);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isForbidden());
    }

    @Test
    void shouldReturnStatus201WhenCreateGameRequestIsCalledWithAuthenticatedUser() throws Exception {
        var request = new CreateGameRequestDto(bob.getId(), OrderedPairCreationType.AS_PASSED);
        givenAuthenticationHappensFor(alice);
        whenCreateGameRequestIsPerformedWith(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isCreated());
        thenHeaderLocationFollowedByIdShouldBe("/chessapi/game/");
        thenIsExpectedFromResponse(
                jsonPath("$.id", notNullValue()),
                jsonPath("$.firstPlayer.id", equalTo(alice.getId().intValue())),
                jsonPath("$.firstPlayer.username", equalTo(alice.getUsername())),
                jsonPath("$.secondPlayer.id", equalTo(bob.getId().intValue())),
                jsonPath("$.secondPlayer.username", equalTo(bob.getUsername()))
        );
    }

    @Test
    void shouldReturnStatus409WhenCreateGameRequestIsCalledWithNonExistingAdversary() throws Exception {
        var request = new CreateGameRequestDto(-1L, OrderedPairCreationType.AS_PASSED);
        givenAuthenticationHappensFor(alice);
        whenCreateGameRequestIsPerformedWith(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
                status().isConflict(),
                jsonPath("$.errors", hasSize(1)),
                jsonPath("$.errors[0]", equalTo("Player not found"))
        );
    }

    @Test
    void shouldReturnStatus400WhenCreateGameRequestIsCalledWithInvalidRequest() throws Exception {
        var request = new CreateGameRequestDto(null, null);
        givenAuthenticationHappensFor(alice);
        whenCreateGameRequestIsPerformedWith(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(
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
        whenCreateGameRequestIsPerformedWith(request);
        thenShouldHaveNoErrors();
        thenIsExpectedFromResponse(status().isForbidden());
    }

    private ResultMatcher[] gameInIndexMatches(Integer index, GameEntity game) {
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
}
