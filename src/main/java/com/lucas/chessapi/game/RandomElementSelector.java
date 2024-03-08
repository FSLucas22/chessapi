package com.lucas.chessapi.game;

import java.util.List;

public class RandomElementSelector<T> {
    public T selectFrom(List<T> elements) {
        return elements.get(0);
    }
}
