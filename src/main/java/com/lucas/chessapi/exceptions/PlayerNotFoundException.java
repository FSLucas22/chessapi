package com.lucas.chessapi.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PlayerNotFoundException extends BusinessException {
    private final Long playerId;

    public PlayerNotFoundException(String message, HttpStatus status, Long playerId) {
        super(message, status);
        this.playerId = playerId;
    }

    public PlayerNotFoundException(String message, Long playerId) {
        super(message, HttpStatus.CONFLICT);
        this.playerId = playerId;
    }
}
