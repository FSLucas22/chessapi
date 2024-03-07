package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.security.ContextJwtFilterTest;
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
        givenPrefixIs("Bearer");
        givenUserIsFound(user, validToken);
        whenFilterIsCalled();
        thenSecurityContextShouldHaveUser();
    }

    @Test
    void shouldPropagateErrorThrownByValidator() {
        var invalidToken = "1234";
        givenPrefixIs("Bearer");
        givenValidatorThrows(new InvalidTokenException("test"), invalidToken);
        whenFilterIsCalled();
        thenShouldCallValidator();
        thenShouldThrow(InvalidTokenException.class, "test");
        thenShouldNeverProcessToken();
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenSubjectDontMatchAUserId() {
        var invalidToken = "1234";
        givenPrefixIs("Bearer");
        givenNoUserIsFound(invalidToken);
        whenFilterIsCalled();
        thenShouldThrow(InvalidTokenException.class, "User not found");
    }

    @Test
    void shouldNotSaveUserInContextWhenHeaderIsNull() {
        givenHeaderIsNullFor("Authorization");
        whenFilterIsCalled();
        thenShouldHaveNoErrors();
        thenShouldNotCallValidator();
        thenShouldNeverProcessToken();
        thenSecurityContextShouldHaveNothing();
    }

    @Test
    void shouldNotSaveUserInContextWhenHeaderDontHavePrefix() {
        givenPrefixIs("Bearer");
        givenHeaderWithoutPrefixFor("Authorization");
        whenFilterIsCalled();
        thenShouldHaveNoErrors();
        thenShouldNotCallValidator();
        thenShouldNeverProcessToken();
        thenSecurityContextShouldHaveNothing();
    }
}
