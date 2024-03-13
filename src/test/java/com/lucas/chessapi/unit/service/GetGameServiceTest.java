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
