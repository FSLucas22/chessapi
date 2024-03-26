package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;
import com.lucas.chessapi.exceptions.GameNotFoundException;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.service.GetGameService;
import com.lucas.chessapi.service.GetUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetGameServiceImpl implements GetGameService {
    private final GameRepository repository;
    private final GetUserService userService;

    @Override
    public GetGameResponseDto getById(Long id) {
        var game = repository.findById(id).orElseThrow(() -> new GameNotFoundException("Game not found"));
        var status = calculateStatus(game);
        var statusIsChanged = !status.equals(game.getStatus());
        if (statusIsChanged) {
            game.setStatus(status);
            repository.save(game);
        }
        return GetGameResponseDto.from(game);
    }

    @Override
    public GetAllGamesResponseDto getAllByUserId(Long id, Integer pageNumber, Integer pageSize) {
        Pageable pagination = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(Sort.Order.asc("updatedAt"))
        );

        var user = getUser(id);
        var gameSlice = repository.findAllByUser(user, pagination);
        var gameList = gameSlice.stream().map(GetGameResponseDto::from).toList();
        return new GetAllGamesResponseDto(PlayerDto.from(user), gameList, gameSlice.hasNext());
    }

    private GameStatus calculateStatus(GameEntity game) {
        if (isExpired(game)) {
            return GameStatus.EXPIRED;
        }

        if (isLostOnTimeBySecondPlayer(game)) {
            return GameStatus.WON_BY_FIRST_PLAYER;
        }

        if (isLostOnTimeByFirstPlayer(game)) {
            return GameStatus.WON_BY_SECOND_PLAYER;
        }

        return game.getStatus();
    }

    private UserEntity getUser(Long id) {
        try {
            return userService.getById(id).toUserEntity();
        } catch (GetUserException e) {
            throw new PlayerNotFoundException("Player not found", id);
        }
    }

    private Boolean timeIsUp(GameEntity game) {
        return game.getFirstPlayerRemainingTimeMillis() == 0 ||
                game.getSecondPlayerRemainingTimeMillis() == 0;
    }

    private Boolean inFirstMoves(GameEntity game) {
        return game.getNumberOfMoves() < 2;
    }

    private Boolean isExpired(GameEntity game) {
        return timeIsUp(game) && inFirstMoves(game);
    }

    private Boolean isLostOnTimeBySecondPlayer(GameEntity game) {
        return timeIsUp(game) && game.getStatus() == GameStatus.WAITING_SECOND_PLAYER && !inFirstMoves(game);
    }

    private Boolean isLostOnTimeByFirstPlayer(GameEntity game) {
        return timeIsUp(game) && game.getStatus() == GameStatus.WAITING_FIRST_PLAYER && !inFirstMoves(game);
    }
}
