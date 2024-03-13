package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.InvalidGameException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.OrderedPair;
import com.lucas.chessapi.game.OrderedPairFactory;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.security.jwt.JwtTokenDto;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import com.lucas.chessapi.service.GetUserService;
import com.lucas.chessapi.service.impl.CreateGameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.lucas.chessapi.builders.DateFactory.today;
import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static com.lucas.chessapi.game.enums.OrderedPairCreationType.AS_PASSED;
import static com.lucas.chessapi.game.enums.OrderedPairCreationType.INVERTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateGameServiceTest {
    @Mock
    GetUserService getUserService;
    @Mock
    TokenProcessor processor;
    @Mock
    GameRepository repository;
    @Mock
    OrderedPairFactory<PlayerDto> factory;
    @InjectMocks
    CreateGameServiceImpl createGameService;

    final String token = "1234";

    @Test
    void shouldCreateGameWhenRequestIsValidWithChallengerFirst() {
        // GIVEN
        var request = new CreateGameRequestDto(2L, AS_PASSED);
        var player = new PlayerDto(1L, "player");
        var adversary = new PlayerDto(2L, "adversary");

        given(user(1L, "player"), user(2L, "adversary"));
        when(factory.fromType(any(), any(), any())).thenReturn(new OrderedPair<>(player, adversary));
        when(processor.getJwtTokenDtoFromToken(token))
                .thenReturn(new JwtTokenDto("1", today(), today()));
        when(repository.save(any())).thenReturn(GameEntityBuilder.valid().id(1L)
                .firstPlayer(player.toUserEntity())
                .secondPlayer(adversary.toUserEntity())
                .build()
        );

        // WHEN
        var result = createGameService.createGame(token, request);

        // THEN
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new CreateGameResponseDto(
                        1L,
                        new PlayerDto(1L, "player"),
                        new PlayerDto(2L, "adversary")
                ));
    }


    @Test
    void shouldCreateGameWhenRequestIsValidWithChallengerSecond() {
        // GIVEN
        var request = new CreateGameRequestDto(2L, INVERTED);
        var player = new PlayerDto(1L, "player");
        var adversary = new PlayerDto(2L, "adversary");

        given(user(1L, "player"), user(2L, "adversary"));
        when(factory.fromType(any(), any(), any())).thenReturn(new OrderedPair<>(adversary, player));
        when(processor.getJwtTokenDtoFromToken(token))
                .thenReturn(new JwtTokenDto("1", today(), today()));
        when(repository.save(any())).thenReturn(GameEntityBuilder.valid()
                .firstPlayer(adversary.toUserEntity())
                .secondPlayer(player.toUserEntity())
                .build());

        // WHEN
        var result = createGameService.createGame(token, request);

        // THEN
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new CreateGameResponseDto(
                        1L,
                        new PlayerDto(2L, "adversary"),
                        new PlayerDto(1L, "player")
                ));
    }

    @Test
    void shouldThrowPlayerNotFoundExceptionWhenUserDontExist() {
        // GIVEN
        var request = new CreateGameRequestDto(2L, INVERTED);

        when(processor.getJwtTokenDtoFromToken(token))
                .thenReturn(new JwtTokenDto("1", today(), today()));
        when(getUserService.getById(1L)).thenThrow(new GetUserException("User not found"));

        // WHEN
        var exception = assertThrows(
                PlayerNotFoundException.class,
                () -> createGameService.createGame(token, request)
        );

        // THEN
        assertThat(exception).hasMessage("Player not found");
        assertThat(exception.getPlayerId()).isEqualTo(1L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowPlayerNotFoundExceptionWhenAdversaryDontExist() {
        // GIVEN
        var request = new CreateGameRequestDto(2L, INVERTED);

        given(user(1L, "challenger"));
        when(processor.getJwtTokenDtoFromToken(token))
                .thenReturn(new JwtTokenDto("1", today(), today()));
        when(getUserService.getById(2L)).thenThrow(new GetUserException("User not found"));

        // WHEN
        var exception = assertThrows(
                PlayerNotFoundException.class,
                () -> createGameService.createGame(token, request)
        );

        // THEN
        assertThat(exception).hasMessage("Player not found");
        assertThat(exception.getPlayerId()).isEqualTo(2L);
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowInvalidGameExceptionWhenAdversaryIsNull() {
        // GIVEN
        var request = new CreateGameRequestDto(null, AS_PASSED);

        // WHEN
        assertThatThrownBy(() -> createGameService.createGame(token, request))
                // THEN
                .isInstanceOf(InvalidGameException.class)
                .hasMessage("Adversary must not be null");
    }

    void given(UserEntity... users) {
        Arrays.stream(users).forEach(user ->
                when(getUserService.getById(user.getId()))
                        .thenReturn(GetUserResponseDto.from(user))
        );
    }
}
