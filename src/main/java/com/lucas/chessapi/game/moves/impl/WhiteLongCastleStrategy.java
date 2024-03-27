package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;

public class WhiteLongCastleStrategy implements MoveStrategy {
    @Override
    public void in(String[][] position) {
        new NormalMoveStrategy("e1", "c1").in(position);
        new NormalMoveStrategy("a1", "d1").in(position);
    }
}
