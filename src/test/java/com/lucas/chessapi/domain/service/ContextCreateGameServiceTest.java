package com.lucas.chessapi.domain.service;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.OrderedPair;
import com.lucas.chessapi.game.OrderedPairFactory;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.security.jwt.JwtTokenDto;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import com.lucas.chessapi.service.GetUserService;
import com.lucas.chessapi.service.impl.CreateGameServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.lucas.chessapi.builders.DateFactory.today;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContextCreateGameServiceTest extends TestContextHelper {

    @Mock
    private GetUserService getUserService;
    @Mock
    private TokenProcessor processor;
    @Mock
    private GameRepository repository;
    @Mock
    private OrderedPairFactory<PlayerDto> factory;
    @InjectMocks
    private CreateGameServiceImpl createGameService;

    private String token;

    private CreateGameResponseDto result;

    protected void givenOrderedPairFactoryReturns(OrderedPair<PlayerDto> pair) {
        when(factory.fromType(any(), any(), any())).thenReturn(pair);
    }

    protected void givenUsers(UserEntity... users) {
        Arrays.stream(users).forEach(user ->
                when(getUserService.getById(user.getId()))
                        .thenReturn(GetUserResponseDto.from(user))
        );
    }

    protected void givenTokenIs(String token, String subject) {
        this.token = token;
        when(processor.getJwtTokenDtoFromToken(token))
                .thenReturn(new JwtTokenDto(subject, today(), today()));
    }

    protected void givenNewGameIdWillBe(Long id) {
        when(repository.save(any())).thenReturn(new GameEntity(id, null, null));
    }

    protected void givenServiceWillThrowFor(Long playerId, Exception exception) {
        when(getUserService.getById(playerId)).thenThrow(exception);
    }

    protected void whenGameIsCreatedFor(CreateGameRequestDto request) {
        try {
            result = createGameService.createGame(token, request);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResultShouldBe(CreateGameResponseDto response) {
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    protected void thenPlayerNotFoundIdShouldBe(Long id) {
        var playerNotFoundException = (PlayerNotFoundException) error;
        assertThat(playerNotFoundException.getPlayerId()).isEqualTo(id);
    }

    protected void thenShouldNotCreateAnyGame() {
        verify(repository, never()).save(any());
    }
}