package com.lucas.chessapi.unit.game;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.game.GameStatusManager;
import com.lucas.chessapi.game.enums.GameStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GameStatusManagerTest {

    GameStatusManager manager;

    @BeforeEach
    void setUp() {
        manager = new GameStatusManager();
    }

    @Test
    void shouldReturnStatusExpiredWhenFirstPlayerDontMakeFirstMove() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(0)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .firstPlayerRemainingTimeMillis(0L)
                .build();

        var status = manager.calculateStatus(game);

        assertThat(status).isEqualTo(GameStatus.EXPIRED);
    }

    @Test
    void shouldReturnStatusExpiredWhenSecondPlayerDontMakeSecondMove() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(1)
                .status(GameStatus.WAITING_SECOND_PLAYER)
                .secondPlayerRemainingTimeMillis(0L)
                .build();

        var status = manager.calculateStatus(game);

        assertThat(status).isEqualTo(GameStatus.EXPIRED);
    }

    @Test
    void shouldReturnStatusWonBySecondPlayerWhenFirstPlayerDontMoveAfterSecondMove() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(2)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .firstPlayerRemainingTimeMillis(0L)
                .build();

        var status = manager.calculateStatus(game);

        assertThat(status).isEqualTo(GameStatus.WON_BY_SECOND_PLAYER);
    }

    @Test
    void shouldReturnStatusWonByFirstPlayerWhenSecondPlayerDontMoveAfterThirdMove() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(3)
                .status(GameStatus.WAITING_SECOND_PLAYER)
                .secondPlayerRemainingTimeMillis(0L)
                .build();

        var status = manager.calculateStatus(game);

        assertThat(status).isEqualTo(GameStatus.WON_BY_FIRST_PLAYER);
    }

    @Test
    void shouldReturnStatusWaitingFirstPlayerWhenNumberOfMovesIsEvenAndTimeIsNotZero() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(2)
                .status(GameStatus.WAITING_SECOND_PLAYER)
                .firstPlayerRemainingTimeMillis(5L)
                .secondPlayerRemainingTimeMillis(5L)
                .build();

        var status = manager.calculateStatus(game);

        assertThat(status).isEqualTo(GameStatus.WAITING_FIRST_PLAYER);
    }

    @Test
    void shouldReturnStatusWaitingSecondPlayerWhenNumberOfMovesIsOddAndTimeIsNotZero() {
        var game = GameEntityBuilder.valid()
                .numberOfMoves(1)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .firstPlayerRemainingTimeMillis(5L)
                .secondPlayerRemainingTimeMillis(5L)
                .build();

        var status = manager.calculateStatus(game);

        assertThat(status).isEqualTo(GameStatus.WAITING_SECOND_PLAYER);
    }

    @Test
    void shouldNotAlterStatusWhenThereIsAlreadyAWinner() {
        var game = GameEntityBuilder.valid()
                .status(GameStatus.WON_BY_SECOND_PLAYER)
                .build();
        var status = manager.calculateStatus(game);
        assertThat(status).isEqualTo(game.getStatus());
    }

    @Test
    void shouldNotAlterStatusWhenGameIsExpired() {
        var game = GameEntityBuilder.valid()
                .status(GameStatus.EXPIRED)
                .build();
        var status = manager.calculateStatus(game);
        assertThat(status).isEqualTo(game.getStatus());
    }

    @Test
    void shouldNotAlterStatusWhenGameIsDrawn() {
        var game = GameEntityBuilder.valid()
                .status(GameStatus.DRAWN)
                .build();
        var status = manager.calculateStatus(game);
        assertThat(status).isEqualTo(game.getStatus());
    }
}
