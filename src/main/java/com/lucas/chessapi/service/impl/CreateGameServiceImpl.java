package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.configuration.GameConfiguration;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.InvalidGameException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.OrderedPair;
import com.lucas.chessapi.game.OrderedPairFactory;
import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import com.lucas.chessapi.service.CreateGameService;
import com.lucas.chessapi.service.GetUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateGameServiceImpl implements CreateGameService {
    private final GetUserService getUserService;
    private final TokenProcessor processor;
    private final GameRepository repository;
    private final OrderedPairFactory<PlayerDto> factory;

    @Override
    public CreateGameResponseDto createGame(String token, CreateGameRequestDto request) {
        validateRequest(request);
        var userId = extractIdFromToken(token);
        var order = defineOrder(userId, request.adversaryId(), request.strategy());

        var newGame = repository.save(
                new GameEntity(
                        null,
                        order.first().toUserEntity(),
                        order.second().toUserEntity(),
                        "",
                        0,
                        GameStatus.WAITING_FIRST_PLAYER,
                        GameConfiguration.WAITING_TIME_MILLIS,
                        GameConfiguration.WAITING_TIME_MILLIS,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                )
        );

        return CreateGameResponseDto.from(newGame);
    }

    private void validateRequest(CreateGameRequestDto request) {
        if (request.adversaryId() == null)
            throw new InvalidGameException("Adversary must not be null");
    }

    private OrderedPair<PlayerDto> defineOrder(
            Long challengerId,
            Long adversaryId,
            OrderedPairCreationType strategyType
    ) {
        var user = getPlayerById(challengerId);
        var adversary = getPlayerById(adversaryId);
        return factory.fromType(user, adversary, strategyType);
    }

    private Long extractIdFromToken(String token) {
        return Long.valueOf(processor.getJwtTokenDtoFromToken(token).subject());
    }

    private PlayerDto getPlayerById(Long id) {
        try {
            return PlayerDto.from(getUserService.getById(id));
        } catch (GetUserException e) {
            throw new PlayerNotFoundException("Player not found", id);
        }
    }
}
