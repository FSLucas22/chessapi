package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.WhiteShortCastleStrategy;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.unit.game.moves.PositionUtils.emptyRank;
import static org.assertj.core.api.Assertions.assertThat;

public class WhiteShortCastleStrategyTest {
    @Test
    void shouldCastleShortForWhite() {
        MoveStrategy move = new WhiteShortCastleStrategy();
        var position = new String[][]{
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                {" ", " ", " ", " ", "K", " ", " ", "R"}
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
                        {" ", " ", " ", " ", " ", "R", "K", " "}
                });
    }
}
