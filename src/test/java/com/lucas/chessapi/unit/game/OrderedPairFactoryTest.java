package com.lucas.chessapi.unit.game;

import com.lucas.chessapi.game.OrderedPair;
import com.lucas.chessapi.game.OrderedPairFactory;
import com.lucas.chessapi.game.RandomElementSelector;
import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.lucas.chessapi.game.enums.OrderedPairCreationType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderedPairFactoryTest {
    @Mock
    private RandomElementSelector<OrderedPairCreationType> elementSelector;

    @InjectMocks
    private OrderedPairFactory<Integer> factory;

    @Test
    void shouldReturnOrderedPairInTheOrderOfTheParameters() {
        assertThat(factory.fromType(1, 2, AS_PASSED))
                .usingRecursiveComparison()
                .isEqualTo(new OrderedPair<>(1, 2));
    }

    @Test
    void shouldReturnOrderedPairInInvertedOrder() {
        assertThat(factory.fromType(1, 2, INVERTED))
                .usingRecursiveComparison()
                .isEqualTo(new OrderedPair<>(2, 1));
    }

    @Test
    void shouldReturnOrderedPairAccordingToTypeReturnedByRandomSelector() {
        when(elementSelector.selectFrom(List.of(AS_PASSED, INVERTED))).thenReturn(AS_PASSED);
        assertThat(factory.fromType(1, 2, RANDOM))
                .usingRecursiveComparison()
                .isEqualTo(new OrderedPair<>(1, 2));
    }
}
