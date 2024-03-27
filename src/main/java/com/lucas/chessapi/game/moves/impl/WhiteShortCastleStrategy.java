package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;

public class WhiteShortCastleStrategy implements MoveStrategy {
    @Override
    public void in(String[][] position) {
        new NormalMoveStrategy("e1", "g1").in(position);
        new NormalMoveStrategy("h1", "f1").in(position);
    }
}
