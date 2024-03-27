package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.WhiteLongCastleStrategy;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.unit.game.moves.PositionUtils.emptyRank;
import static org.assertj.core.api.Assertions.assertThat;

public class WhiteLongCastleStrategyTest {
    @Test
    void shouldCastleLongForWhite() {
        MoveStrategy move = new WhiteLongCastleStrategy();
        var position = new String[][]{
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                {"R", " ", " ", " ", "K", " ", " ", " "}
        };

        move.in(position);

        assertThat(position)
                .isEqualTo(new String[][]{emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        emptyRank(),
                        {" ", " ", "K", "R", " ", " ", " ", " "}
                });
    }
}
