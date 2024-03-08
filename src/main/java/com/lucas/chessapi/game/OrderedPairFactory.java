package com.lucas.chessapi.game;

import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.lucas.chessapi.game.enums.OrderedPairCreationType.AS_PASSED;
import static com.lucas.chessapi.game.enums.OrderedPairCreationType.INVERTED;

@RequiredArgsConstructor
public class OrderedPairFactory<T> {
    private final RandomElementSelector<OrderedPairCreationType> elementSelector;

    public OrderedPair<T> fromType(T first, T second, OrderedPairCreationType type) {
        return switch (type) {
            case AS_PASSED -> asPassed(first, second);
            case INVERTED -> inverted(first, second);
            case RANDOM -> {
                var newType = elementSelector.selectFrom(List.of(AS_PASSED, INVERTED));
                yield fromType(first, second, newType);
            }
        };
    }

    public static <T> OrderedPair<T> asPassed(T first, T second) {
        return new OrderedPair<>(first, second);
    }

    public static <T> OrderedPair<T> inverted(T first, T second) {
        return new OrderedPair<>(second, first);
    }
}
