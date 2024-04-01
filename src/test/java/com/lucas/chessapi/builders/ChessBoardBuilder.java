package com.lucas.chessapi.builders;

import java.util.Arrays;

import static com.lucas.chessapi.game.moves.MoveStrategy.squareToIndex;
import static com.lucas.chessapi.unit.game.moves.PositionUtils.emptyRank;

public class ChessBoardBuilder {
    private final String[][] board;

    public ChessBoardBuilder() {
        board = new String[][]{
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank()
        };
    }

    public String[][] build() {
        return Arrays.stream(board).toArray(String[][]::new);
    }

    public ChessBoardBuilder square(String square, String pieceSymbol) {
        var squareIndex = squareToIndex(square);
        board[squareIndex[0]][squareIndex[1]] = pieceSymbol;
        return this;
    }
}
