package com.lucas.chessapi.game.moves.impl;

import com.lucas.chessapi.game.moves.MoveStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NormalMoveStrategy implements MoveStrategy {
    private final String fromSquare;
    private final String toSquare;

    @Override
    public void in(String[][] position) {
        var fromSquareIndex = squareToIndex(fromSquare);
        var toSquareIndex = squareToIndex(toSquare);

        var piece = position[fromSquareIndex[0]][fromSquareIndex[1]];
        position[fromSquareIndex[0]][fromSquareIndex[1]] = " ";
        position[toSquareIndex[0]][toSquareIndex[1]] = piece;
    }

    private int[] squareToIndex(String square) {
        var columns = "abcdefgh";
        var column = columns.indexOf(square.charAt(0));
        var row = square.charAt(1);
        return new int[]{8 - Integer.parseInt(String.valueOf(row)), column};
    }
}
