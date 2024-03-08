package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.domain.service.ContextCreateGameServiceTest;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.InvalidGameException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.OrderedPair;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static com.lucas.chessapi.game.enums.OrderedPairCreationType.AS_PASSED;
import static com.lucas.chessapi.game.enums.OrderedPairCreationType.INVERTED;

public class CreateGameServiceTest extends ContextCreateGameServiceTest {
    @Test
    void shouldCreateGameWhenRequestIsValidWithChallengerFirst() {
        var request = new CreateGameRequestDto(2L, AS_PASSED);
        var player = new PlayerDto(1L, "player");
        var adversary = new PlayerDto(2L, "adversary");

        givenOrderedPairFactoryReturns(new OrderedPair<>(player, adversary));
        givenUsers(user(1L, "player"), user(2L, "adversary"));
        givenTokenIs("1234", "1");
        givenNewGameIdWillBe(1L);
        whenGameIsCreatedFor(request);
        thenShouldHaveNoErrors();
        thenResultShouldBe(new CreateGameResponseDto(
                1L,
                new PlayerDto(1L, "player"),
                new PlayerDto(2L, "adversary")
        ));
    }

    @Test
    void shouldCreateGameWhenRequestIsValidWithChallengerSecond() {
        var request = new CreateGameRequestDto(2L, INVERTED);
        var player = new PlayerDto(1L, "player");
        var adversary = new PlayerDto(2L, "adversary");
        givenOrderedPairFactoryReturns(new OrderedPair<>(adversary, player));
        givenUsers(user(1L, "player"), user(2L, "adversary"));
        givenTokenIs("1234", "1");
        givenNewGameIdWillBe(1L);
        whenGameIsCreatedFor(request);
        thenShouldHaveNoErrors();
        thenResultShouldBe(new CreateGameResponseDto(
                1L,
                new PlayerDto(2L, "adversary"),
                new PlayerDto(1L, "player")
        ));
    }

    @Test
    void shouldThrowPlayerNotFoundExceptionWhenUserDontExist() {
        var request = new CreateGameRequestDto(2L, INVERTED);
        givenTokenIs("1234", "1");
        givenServiceWillThrowFor(1L, new GetUserException("User not found"));
        whenGameIsCreatedFor(request);
        thenShouldThrow(PlayerNotFoundException.class, "Player not found");
        thenPlayerNotFoundIdShouldBe(1L);
        thenShouldNotCreateAnyGame();
    }

    @Test
    void shouldThrowPlayerNotFoundExceptionWhenAdversaryDontExist() {
        var request = new CreateGameRequestDto(2L, INVERTED);
        givenTokenIs("1234", "1");
        givenUsers(user(1L, "challenger"));
        givenServiceWillThrowFor(2L, new GetUserException("User not found"));
        whenGameIsCreatedFor(request);
        thenShouldThrow(PlayerNotFoundException.class, "Player not found");
        thenPlayerNotFoundIdShouldBe(2L);
        thenShouldNotCreateAnyGame();
    }

    @Test
    void shouldThrowInvalidGameExceptionWhenAdversaryIsNull() {
        var request = new CreateGameRequestDto(null, AS_PASSED);
        whenGameIsCreatedFor(request);
        thenShouldThrow(InvalidGameException.class, "Adversary must not be null");
    }
}
