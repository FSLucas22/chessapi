package com.lucas.chessapi.game;

import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.model.GameEntity;

import java.util.List;

public class GameStatusManager {
    public GameStatus calculateStatus(GameEntity game) {
        if (wasAlreadyFinished(game)) {
            return game.getStatus();
        }

        if (!timeIsUp(game)) {
            return calculateOnGoingStatus(game);
        }

        return calculateGameFinishStatus(game);
    }

    private Boolean wasAlreadyFinished(GameEntity game) {
        var finishingStatus = List.of(
                GameStatus.WON_BY_FIRST_PLAYER,
                GameStatus.WON_BY_SECOND_PLAYER,
                GameStatus.EXPIRED,
                GameStatus.DRAWN
        );
        return finishingStatus.contains(game.getStatus());
    }

    private Boolean timeIsUp(GameEntity game) {
        return game.getFirstPlayerRemainingTimeMillis() == 0 || game.getSecondPlayerRemainingTimeMillis() == 0;
    }

    private GameStatus calculateOnGoingStatus(GameEntity game) {
        if (game.getNumberOfMoves() % 2 == 0)
            return GameStatus.WAITING_FIRST_PLAYER;
        return GameStatus.WAITING_SECOND_PLAYER;
    }

    private GameStatus calculateGameFinishStatus(GameEntity game) {
        if (isInFirstMoves(game))
            return GameStatus.EXPIRED;

        if (game.getStatus() == GameStatus.WAITING_FIRST_PLAYER)
            return GameStatus.WON_BY_SECOND_PLAYER;
        return GameStatus.WON_BY_FIRST_PLAYER;
    }

    private Boolean isInFirstMoves(GameEntity game) {
        return game.getNumberOfMoves() < 2;
    }
}
