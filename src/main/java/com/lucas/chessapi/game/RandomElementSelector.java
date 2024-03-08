package com.lucas.chessapi.game;

import com.lucas.chessapi.exceptions.InvalidArgumentException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class RandomElementSelector<T> {
    private final Random randomGenerator;

    public T selectFrom(List<T> elements) {
        if (elements.isEmpty())
            throw new InvalidArgumentException("List cannot be empty");

        var index = randomGenerator.nextInt(0, elements.size());
        return elements.get(index);
    }
}
