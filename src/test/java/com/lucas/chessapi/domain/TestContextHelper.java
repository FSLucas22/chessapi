package com.lucas.chessapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TestContextHelper {
    protected Exception error;

    protected void thenShouldThrow(Class<? extends Exception> exceptionClass, String expectedMessage) {
        assertThat(error)
                .isInstanceOf(exceptionClass)
                .hasMessage(expectedMessage);
    }

    protected void thenNothingHappens() {
        assertThat(error).isNull();
    }
}
