package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlackEnPassantMoveStrategy implements MoveStrategy {
    private final String fromSquare;
    private final String captureSquare;

    @Override
    public void in(String[][] position) {
        var fromIndex = MoveStrategy.squareToIndex(fromSquare);
        var captureIndex = MoveStrategy.squareToIndex(captureSquare);
        position[captureIndex[0] + 1][captureIndex[1]] = position[fromIndex[0]][fromIndex[1]];
        position[fromIndex[0]][fromIndex[1]] = " ";
        position[captureIndex[0]][captureIndex[1]] = " ";
    }
}
