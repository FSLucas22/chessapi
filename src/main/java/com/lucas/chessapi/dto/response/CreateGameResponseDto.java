package com.lucas.chessapi.dto.response;


import com.lucas.chessapi.configuration.GameConfiguration;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.model.GameEntity;

public record CreateGameResponseDto(
        Long id,
        PlayerDto firstPlayer,
        PlayerDto secondPlayer,
        String moves,
        Integer numberOfMoves,
        GameStatus status,
        Long firstPlayerRemainingTimeMilis,
        Long secondPlayerRemainingTimeMilis
) {
    public static CreateGameResponseDto from(GameEntity game) {
        var firstPlayer = PlayerDto.from(game.getFirstPlayer());
        var secondPlayer = PlayerDto.from(game.getSecondPlayer());
        return new CreateGameResponseDto(
                game.getId(),
                firstPlayer,
                secondPlayer,
                "",
                0,
                GameStatus.WAITING_FIRST_PLAYER,
                GameConfiguration.WAITING_TIME_MILLIS,
                GameConfiguration.WAITING_TIME_MILLIS
        );
    }
}
