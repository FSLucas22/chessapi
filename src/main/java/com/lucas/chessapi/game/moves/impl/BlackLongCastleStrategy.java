package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;

public class BlackLongCastleStrategy implements MoveStrategy {
    @Override
    public void in(String[][] position) {
        new NormalMoveStrategy("e8", "c8").in(position);
        new NormalMoveStrategy("a8", "d8").in(position);
    }
}
