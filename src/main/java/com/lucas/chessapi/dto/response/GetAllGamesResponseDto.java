package com.lucas.chessapi.dto.response;

import com.lucas.chessapi.dto.game.PlayerDto;

import java.util.List;

public record GetAllGamesResponseDto(
        PlayerDto player,
        List<GetGameResponseDto> games,
        Boolean hasNext
) {
}
