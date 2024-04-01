package com.lucas.chessapi.game.moves;

public interface MoveStrategy {
    void in(String[][] position);

    static int[] squareToIndex(String square) {
        var columns = "abcdefgh";
        var column = columns.indexOf(square.charAt(0));
        var row = square.charAt(1);
        return new int[]{8 - Integer.parseInt(String.valueOf(row)), column};
    }
}