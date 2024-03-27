package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.NormalMoveStrategy;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.unit.game.moves.PositionUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class NormalMoveStrategyTest {

    @Test
    void shouldReturnCurrentPositionAfterMove() {
        MoveStrategy move = new NormalMoveStrategy("e2", "e4");

        var position = initialPosition();

        move.in(position);
        assertThat(position).isEqualTo(
                new String[][]{
                        blackPiecesRank(),
                        blackPawnsRank(),
                        emptyRank(),
                        emptyRank(),
                        {" ", " ", " ", " ", "P", " ", " ", " "},
                        emptyRank(),
                        {"P", "P", "P", "P", " ", "P", "P", "P"},
                        whitePiecesRank()
                }
        );
    }
}
