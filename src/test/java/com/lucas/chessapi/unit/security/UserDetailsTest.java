package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.security.ContextUserDetailsTest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserDetailsTest extends ContextUserDetailsTest {
    @Test
    void shouldCreateUserDetailsBasedOnUser() {
        var user = UserEntityBuilderExtension.valid().build();
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
