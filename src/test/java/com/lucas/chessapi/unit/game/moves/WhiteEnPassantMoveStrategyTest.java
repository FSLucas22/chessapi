package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.builders.ChessBoardBuilder;
import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.WhiteEnPassantMoveStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WhiteEnPassantMoveStrategyTest {
    @Test
    void shouldCaptureLeftSidePawn() {
        MoveStrategy move = new WhiteEnPassantMoveStrategy("e5", "d5");
        var position = new ChessBoardBuilder()
                .square("e5", "P")
                .square("d5", "p")
                .build();

        move.in(position);

        assertThat(position)
                .isEqualTo(new ChessBoardBuilder()
                        .square("d6", "P")
                        .build());
    }

    @Test
    void shouldCaptureRightSidePawn() {
        MoveStrategy move = new WhiteEnPassantMoveStrategy("d5", "e5");
        var position = new ChessBoardBuilder()
                .square("d5", "P")
                .square("e5", "p")
                .build();

        move.in(position);

        assertThat(position)
                .isEqualTo(new ChessBoardBuilder()
                        .square("e6", "P")
                        .build());
    }
}
