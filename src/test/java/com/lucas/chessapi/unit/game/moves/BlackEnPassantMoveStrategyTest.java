package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.builders.ChessBoardBuilder;
import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.BlackEnPassantMoveStrategy;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BlackEnPassantMoveStrategyTest {
    @Test
    void shouldCaptureLeftSidePawn() {
        MoveStrategy move = new BlackEnPassantMoveStrategy("e4", "d4");
        var position = new ChessBoardBuilder()
                .square("d4", "P")
                .square("e4", "p").build();

        move.in(position);

        assertThat(position)
                .isEqualTo(new ChessBoardBuilder().square("d3", "p").build());
    }

    @Test
    void shouldCaptureRightSidePawn() {
        MoveStrategy move = new BlackEnPassantMoveStrategy("d4", "e4");
        var position = new ChessBoardBuilder()
                .square("e4", "P")
                .square("d4", "p").build();

        move.in(position);

        assertThat(position)
                .isEqualTo(new ChessBoardBuilder()
                        .square("e3", "p")
                        .build());
    }
}
