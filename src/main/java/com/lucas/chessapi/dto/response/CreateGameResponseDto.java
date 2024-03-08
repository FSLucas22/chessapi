package com.lucas.chessapi.dto.response;


import com.lucas.chessapi.dto.game.PlayerDto;

public record CreateGameResponseDto(Long id, PlayerDto firstPlayer, PlayerDto secondPlayer) {
}
