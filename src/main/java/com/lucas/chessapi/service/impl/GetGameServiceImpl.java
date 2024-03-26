package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;
import com.lucas.chessapi.exceptions.GameNotFoundException;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.exceptions.PlayerNotFoundException;
import com.lucas.chessapi.game.GameStatusManager;
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
    private final GameStatusManager gameStatusManager;

    @Override
    public GetGameResponseDto getById(Long id) {
        var game = repository.findById(id).orElseThrow(() -> new GameNotFoundException("Game not found"));
        var status = gameStatusManager.calculateStatus(game);
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

    private UserEntity getUser(Long id) {
        try {
            return userService.getById(id).toUserEntity();
        } catch (GetUserException e) {
            throw new PlayerNotFoundException("Player not found", id);
        }
    }

}
