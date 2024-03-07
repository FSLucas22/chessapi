package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.security.ContextUserDetailsTest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserDetailsTest extends ContextUserDetailsTest {
    @Test
    void shouldCreateUserDetailsBasedOnUser() {
        var user = UserEntityBuilder.valid().build();
        givenUser(user);
        whenUserDetailsIsCreated();
        thenShouldHaveNoErrors();
        thenUserDetailsAuthoritiesShouldBe(List.of());
        thenUserDetailsUsernameShouldBe(user.getUsername());
        thenUserDetailsPasswordShouldBe(user.getPassword());
        thenUserDetailsIsAccountNonExpiredShouldBe(true);
        thenUserDetailsIsCredentialsNonExpiredShouldBe(true);
        thenUserDetailsIsAccountNonLockedShouldBe(true);
        thenUserDetailsIsEnabledShouldBe(true);
    }
}
