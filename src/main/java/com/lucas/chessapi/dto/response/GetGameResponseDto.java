package com.lucas.chessapi.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.model.GameEntity;

import java.time.LocalDateTime;

public record GetGameResponseDto(
        Long id,
        PlayerDto firstPlayer,
        PlayerDto secondPlayer,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime updatedAt
) {
    public static GetGameResponseDto from(GameEntity game) {
        return new GetGameResponseDto(
                game.getId(),
                PlayerDto.from(game.getFirstPlayer()),
                PlayerDto.from(game.getSecondPlayer()),
                game.getCreatedAt(),
                game.getUpdatedAt()
        );
    }
}
