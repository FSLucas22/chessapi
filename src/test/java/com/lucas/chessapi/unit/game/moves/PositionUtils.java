package com.lucas.chessapi.unit.game.moves;

public class PositionUtils {
    public static String[][] initialPosition() {
        return new String[][]{
                blackPiecesRank(),
                blackPawnsRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                emptyRank(),
                whitePawnsRank(),
                whitePiecesRank()
        };
    }

    public static String[] whitePiecesRank() {
        return new String[]{"R", "N", "B", "K", "Q", "B", "N", "R"};
    }

    public static String[] whitePawnsRank() {
        return new String[]{"P", "P", "P", "P", "P", "P", "P", "P"};
    }

    public static String[] blackPawnsRank() {
        return new String[]{"p", "p", "p", "p", "p", "p", "p", "p"};
    }

    public static String[] blackPiecesRank() {
        return new String[]{"r", "n", "b", "k", "q", "b", "n", "r"};
    }

    public static String[] emptyRank() {
        return new String[]{" ", " ", " ", " ", " ", " ", " ", " "};
    }
}
