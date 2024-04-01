package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NormalMoveStrategy implements MoveStrategy {
    private final String fromSquare;
    private final String toSquare;

    @Override
    public void in(String[][] position) {
        var fromSquareIndex = MoveStrategy.squareToIndex(fromSquare);
        var toSquareIndex = MoveStrategy.squareToIndex(toSquare);

        var piece = position[fromSquareIndex[0]][fromSquareIndex[1]];
        position[fromSquareIndex[0]][fromSquareIndex[1]] = " ";
        position[toSquareIndex[0]][toSquareIndex[1]] = piece;
    }
}
