package com.lucas.chessapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContextHelper {
    protected Exception error;

    protected void thenShouldThrow(Class<? extends Exception> exceptionClass, String expectedMessage) {
        assertThat(error)
                .isInstanceOf(exceptionClass)
                .hasMessage(expectedMessage);
    }

    protected void thenShouldThrowContaining(
            Class<? extends Exception> exceptionClass,
            String... messageParts
    ) {
        assertThat(error)
                .isInstanceOf(exceptionClass)
                .hasMessageContainingAll(messageParts);
    }

    protected void thenShouldHaveNoErrors() {
        assertThat(error).isNull();
    }
}
