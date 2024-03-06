package com.lucas.chessapi;

import java.util.Random;

public class Utils {
    public static String generateRandomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        return new Random().ints(leftLimit, rightLimit + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
