package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.security.ContextUserDetailsServiceTest;
import com.lucas.chessapi.security.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceTest extends ContextUserDetailsServiceTest {
    @Test
    void shouldLoadUserByEmail() {
        var user = UserEntityBuilder
                .valid()
                .email("test@email.com")
                .build();
        givenUserExists(user);
        whenLoadByUsernameIsCalledWith("test@email.com");
        thenShouldHaveNoErrors();
        thenShouldReturn(new UserDetailsImpl(user));
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserIsNotFound() {
        whenLoadByUsernameIsCalledWith("test@email.com");
        thenShouldThrow(UsernameNotFoundException.class, "test@email.com");
    }
}
