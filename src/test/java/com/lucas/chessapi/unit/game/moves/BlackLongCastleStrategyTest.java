package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.BlackLongCastleStrategy;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.unit.game.moves.PositionUtils.emptyRank;
import static org.assertj.core.api.Assertions.assertThat;

public class BlackLongCastleStrategyTest {
    @Test
    void shouldCastleLongForBlack() {
        MoveStrategy move = new BlackLongCastleStrategy();
        var position = new String[][]{
                {"r", " ", " ", " ", "k", " ", " ", " "},
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank()
        };

        move.in(position);

        assertThat(position)
                .isEqualTo(new String[][]{
                        {" ", " ", "k", "r", " ", " ", " ", " "},
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank()
                });
    }

}
