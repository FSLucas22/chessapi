package com.lucas.chessapi.dto.response;


import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.model.GameEntity;

public record CreateGameResponseDto(Long id, PlayerDto firstPlayer, PlayerDto secondPlayer) {
    public static CreateGameResponseDto from(GameEntity game) {
        var firstPlayer = PlayerDto.from(game.getFirstPlayer());
        var secondPlayer = PlayerDto.from(game.getSecondPlayer());
        return new CreateGameResponseDto(game.getId(), firstPlayer, secondPlayer);
    }
}
