package com.lucas.chessapi.unit;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.ContextJwtFilterTest;
import com.lucas.chessapi.exceptions.InvalidTokenException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class JwtFilterTest extends ContextJwtFilterTest {

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetUserAuthenticationWhenTokenIsValid() throws ServletException, IOException {
        var validToken = "1234";
        var user = UserEntityBuilder.validUserEntity();

        givenUserIsFound(user, validToken);
        whenFilterIsCalled();
        thenSecurityContextShouldHaveUser();
    }

    @Test
    void shouldPropagateErrorThrownByValidator() throws ServletException, IOException {
        var invalidToken = "1234";

        givenValidatorThrows(new InvalidTokenException("test"), invalidToken);
        whenFilterIsCalled();
        thenShouldThrow(InvalidTokenException.class, "test");
        thenShouldNeverProcessToken();
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenSubjectDontMatchAUserId() {
        var invalidToken = "1234";
        givenNoUserIsFound(invalidToken);
        whenFilterIsCalled();
        thenShouldThrow(InvalidTokenException.class, "User not found");
    }
}
