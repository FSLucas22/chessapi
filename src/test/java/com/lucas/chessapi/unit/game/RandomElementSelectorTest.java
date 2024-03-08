package com.lucas.chessapi.unit.game;

import com.lucas.chessapi.exceptions.InvalidArgumentException;
import com.lucas.chessapi.game.RandomElementSelector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RandomElementSelectorTest {
    @Mock
    Random randomGenerator;

    @InjectMocks
    private RandomElementSelector<Integer> elementSelector;

    @Test
    void shouldUserIntegerReturnedByRandomGeneratorAsIndex() {
        var items = List.of(1, 2, 3);
        when(randomGenerator.nextInt(0, 3)).thenReturn(2);
        assertThat(elementSelector.selectFrom(items)).isEqualTo(3);
    }

    @Test
    void shouldThrowInvalidArgumentExceptionWhenListIsEmpty() {
        assertThatThrownBy(() -> elementSelector.selectFrom(List.of()))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("List cannot be empty");
    }
}
