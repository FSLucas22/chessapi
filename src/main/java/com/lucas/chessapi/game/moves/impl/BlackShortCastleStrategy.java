package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;

public class BlackShortCastleStrategy implements MoveStrategy {
    @Override
    public void in(String[][] position) {
        new NormalMoveStrategy("e8", "g8").in(position);
        new NormalMoveStrategy("h8", "f8").in(position);
    }
}
