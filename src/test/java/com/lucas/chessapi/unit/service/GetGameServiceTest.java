package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GameNotFoundException;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.service.GetUserService;
import com.lucas.chessapi.service.impl.GetGameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetGameServiceTest {
    @Mock
    GameRepository repository;

    @Mock
    GetUserService userService;

    @InjectMocks
    GetGameServiceImpl service;

    @Test
    void shouldReturnGameById() {
        var game = GameEntityBuilder.valid().id(1L).build();
        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        var result = service.getById(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetGameResponseDto(
                        game.getId(),
                        new PlayerDto(firstPlayer.getId(), firstPlayer.getUsername()),
                        new PlayerDto(secondPlayer.getId(), secondPlayer.getUsername()),
                        game.getMoves(),
                        game.getNumberOfMoves(),
                        game.getStatus(),
                        game.getFirstPlayerRemainingTimeMillis(),
                        game.getSecondPlayerRemainingTimeMillis(),
                        game.getCreatedAt(),
                        game.getUpdatedAt()
                ));
    }

    @Test
    void shouldThrowGameNotFoundExceptionWhenGameDontExist() {
        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage("Game not found");
    }

    @Test
    void shouldGetAllGamesByUserIdPaginated() {
        var player = UserEntityBuilder.getBuilder().id(5L).build();
        var game1 = game(1L).firstPlayer(player).build();
        var game2 = game(2L).secondPlayer(player).build();

        given(player);
        givenGamesFromUser(game1, game2);

        var allGamesResult = service.getAllByUserId(5L, 0, 3);

        assertThat(allGamesResult)
                .usingRecursiveComparison()
                .isEqualTo(new GetAllGamesResponseDto(
                        PlayerDto.from(player),
                        List.of(GetGameResponseDto.from(game1), GetGameResponseDto.from(game2)),
                        false
                ));
    }

    @Test
    void shouldThrowPlayerNotFoundExceptionWhenUserDontExist() {
        when(userService.getById(any())).thenThrow(new GetUserException("User not found"));

        var exception = assertThrows(
                PlayerNotFoundException.class,
                () -> service.getAllByUserId(1L, 0, 3)
        );

        assertThat(exception).hasMessage("Player not found");
        assertThat(exception.getPlayerId()).isEqualTo(1L);
        verify(repository, never()).findAllByUser(any(), any());
        verify(repository, never()).findById(any());
    }

    @Test
    void shouldExpireGameWhenFirstPlayerDontMove() {
        var game = GameEntityBuilder.valid()
                .id(1L).status(GameStatus.WAITING_FIRST_PLAYER)
                .firstPlayerRemainingTimeMillis(0L)
                .build();
        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));
        when(repository.save(game))
                .thenReturn(game);

        var result = service.getById(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetGameResponseDto(
                        game.getId(),
                        PlayerDto.from(firstPlayer),
                        PlayerDto.from(secondPlayer),
                        game.getMoves(),
                        game.getNumberOfMoves(),
                        GameStatus.EXPIRED,
                        0L,
                        game.getSecondPlayerRemainingTimeMillis(),
                        game.getCreatedAt(),
                        game.getUpdatedAt()
                ));

        verify(repository, times(1)).save(game);
    }

    @Test
    void shouldExpireGameWhenSecondPlayerDontMove() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(1)
                .id(1L).status(GameStatus.WAITING_SECOND_PLAYER)
                .secondPlayerRemainingTimeMillis(0L)
                .build();

        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));
        when(repository.save(game))
                .thenReturn(game);

        var result = service.getById(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetGameResponseDto(
                        game.getId(),
                        PlayerDto.from(firstPlayer),
                        PlayerDto.from(secondPlayer),
                        game.getMoves(),
                        game.getNumberOfMoves(),
                        GameStatus.EXPIRED,
                        game.getFirstPlayerRemainingTimeMillis(),
                        0L,
                        game.getCreatedAt(),
                        game.getUpdatedAt()
                ));

        verify(repository, times(1)).save(game);
    }

    @Test
    void shouldGiveFirstPlayerWinWhenSecondPlayerLoseOnTime() {
        var game = GameEntityBuilder.valid()
                .id(1L)
                .numberOfMoves(4)
                .status(GameStatus.WAITING_SECOND_PLAYER)
                .secondPlayerRemainingTimeMillis(0L)
                .build();

        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));
        when(repository.save(game))
                .thenReturn(game);

        var result = service.getById(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetGameResponseDto(
                        game.getId(),
                        PlayerDto.from(firstPlayer),
                        PlayerDto.from(secondPlayer),
                        game.getMoves(),
                        game.getNumberOfMoves(),
                        GameStatus.WON_BY_FIRST_PLAYER,
                        game.getFirstPlayerRemainingTimeMillis(),
                        0L,
                        game.getCreatedAt(),
                        game.getUpdatedAt()
                ));

        verify(repository, times(1)).save(game);
    }

    @Test
    void shouldGiveSecondPlayerWinWhenFirstPlayerLoseOnTime() {
        var game = GameEntityBuilder.valid()
                .id(1L)
                .numberOfMoves(3)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .firstPlayerRemainingTimeMillis(0L)
                .build();

        var firstPlayer = game.getFirstPlayer();
        var secondPlayer = game.getSecondPlayer();
        when(repository.findById(game.getId()))
                .thenReturn(Optional.of(game));
        when(repository.save(game))
                .thenReturn(game);

        var result = service.getById(1L);

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetGameResponseDto(
                        game.getId(),
                        PlayerDto.from(firstPlayer),
                        PlayerDto.from(secondPlayer),
                        game.getMoves(),
                        game.getNumberOfMoves(),
                        GameStatus.WON_BY_SECOND_PLAYER,
                        0L,
                        game.getSecondPlayerRemainingTimeMillis(),
                        game.getCreatedAt(),
                        game.getUpdatedAt()
                ));

        verify(repository, times(1)).save(game);
    }

    GameEntityBuilder game(Long id) {
        return GameEntityBuilder.valid().id(id);
    }

    void given(UserEntity... users) {
        Arrays.stream(users).forEach(
                u -> when(userService.getById(u.getId()))
                        .thenReturn(GetUserResponseDto.from(u))
        );
    }

    void givenGamesFromUser(GameEntity... games) {
        Slice<GameEntity> gameSlice = new SliceImpl<>(List.of(games));
        when(repository.findAllByUser(any(), any())).thenReturn(gameSlice);
    }
}
