package com.lucas.chessapi.dto.request;

import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import jakarta.validation.constraints.NotNull;

public record CreateGameRequestDto(
        @NotNull
        Long adversaryId,
        @NotNull
        OrderedPairCreationType strategy) {
}
