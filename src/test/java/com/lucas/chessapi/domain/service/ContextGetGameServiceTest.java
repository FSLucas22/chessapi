package com.lucas.chessapi.domain.service;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.service.GetUserService;
import com.lucas.chessapi.service.impl.GetGameServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContextGetGameServiceTest extends TestContextHelper {
    @Mock
    GameRepository repository;

    @Mock
    GetUserService userService;

    @InjectMocks
    GetGameServiceImpl service;

    private GetGameResponseDto result;
    private GetAllGamesResponseDto allGamesResult;

    protected void given(GameEntity... games) {
        Arrays.stream(games).forEach(
                g -> when(repository.findById(g.getId()))
                        .thenReturn(Optional.of(g))
        );
    }

    protected void given(UserEntity... users) {
        Arrays.stream(users).forEach(
                u -> when(userService.getById(u.getId()))
                        .thenReturn(GetUserResponseDto.from(u))
        );
    }

    protected void givenGamesFromUser(GameEntity... games) {
        Slice<GameEntity> gameSlice = new SliceImpl<>(List.of(games));
        when(repository.findAllByUser(any(), any())).thenReturn(gameSlice);
    }

    protected void givenUserServiceWillThrow(GetUserException exception) {
        when(userService.getById(any())).thenThrow(exception);
    }

    protected void whenGetByIdIsCalledWith(Long gameId) {
        try {
            result = service.getById(gameId);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void whenGetAllByUserIdIsCalledWith(Long id) {
        try {
            allGamesResult = service.getAllByUserId(id, 0, 3);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResultShouldMatch(GetGameResponseDto response) {
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    protected void thenAllGamesResultShoudMatch(GetAllGamesResponseDto response) {
        assertThat(allGamesResult)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    protected void thenShouldNotInteractWithGameRepository() {
        verify(repository, never()).findAllByUser(any(), any());
        verify(repository, never()).findById(any());
    }


    protected void thenPlayerNotFoundIdShouldBe(Long id) {
        var exception = (PlayerNotFoundException) error;
        assertThat(exception.getPlayerId()).isEqualTo(id);
    }
}
