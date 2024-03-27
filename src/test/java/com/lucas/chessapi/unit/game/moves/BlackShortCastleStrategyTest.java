package com.lucas.chessapi.unit.game.moves;

import com.lucas.chessapi.game.moves.MoveStrategy;
import com.lucas.chessapi.game.moves.impl.BlackShortCastleStrategy;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.unit.game.moves.PositionUtils.emptyRank;
import static org.assertj.core.api.Assertions.assertThat;

public class BlackShortCastleStrategyTest {
    @Test
    void shouldCastleShortForBlack() {
        MoveStrategy move = new BlackShortCastleStrategy();
        var position = new String[][]{
                {" ", " ", " ", " ", "k", " ", " ", "r"},
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
                        {" ", " ", " ", " ", " ", "r", "k", " "},
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
