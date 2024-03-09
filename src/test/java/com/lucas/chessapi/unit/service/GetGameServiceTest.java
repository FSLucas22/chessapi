package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.service.ContextGetGameServiceTest;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;
import com.lucas.chessapi.exceptions.GameNotFoundException;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GetGameServiceTest extends ContextGetGameServiceTest {

    @Test
    void shouldReturnGameById() {
        var game = GameEntityBuilder.valid().id(1L).build();
        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        given(game);
        whenGetByIdIsCalledWith(1L);
        thenShouldHaveNoErrors();
        thenResultShouldMatch(new GetGameResponseDto(
                game.getId(),
                new PlayerDto(firstPlayer.getId(), firstPlayer.getUsername()),
                new PlayerDto(secondPlayer.getId(), secondPlayer.getUsername()),
                game.getCreatedAt(),
                game.getUpdatedAt()
        ));
    }

    @Test
    void shouldThrowGameNotFoundExceptionWhenGameDontExist() {
        whenGetByIdIsCalledWith(1L);
        thenShouldThrow(GameNotFoundException.class, "Game not found");
    }

    @Test
    void shouldGetAllGamesByUserIdPaginated() {
        var player = UserEntityBuilder.getBuilder().id(5L).build();
        var game1 = game(1L).firstPlayer(player).build();
        var game2 = game(2L).secondPlayer(player).build();

        given(player);
        givenGamesFromUser(game1, game2);
        whenGetAllByUserIdIsCalledWith(5L);
        thenShouldHaveNoErrors();
        thenAllGamesResultShoudMatch(new GetAllGamesResponseDto(
                PlayerDto.from(player),
                List.of(GetGameResponseDto.from(game1), GetGameResponseDto.from(game2)),
                false
        ));
    }

    @Test
    void shouldThrowPlayerNotFoundExceptionWhenUserDontExist() {
        givenUserServiceWillThrow(new GetUserException("User not found"));
        whenGetAllByUserIdIsCalledWith(1L);
        thenShouldThrow(PlayerNotFoundException.class, "Player not found");
        thenPlayerNotFoundIdShouldBe(1L);
        thenShouldNotInteractWithGameRepository();
    }

    private GameEntityBuilder game(Long id) {
        return GameEntityBuilder.valid().id(id);
    }
}
