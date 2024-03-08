package com.lucas.chessapi.dto.request;

import com.lucas.chessapi.game.enums.OrderedPairCreationType;

public record CreateGameRequestDto(Long adversaryId, OrderedPairCreationType strategy) {
}
