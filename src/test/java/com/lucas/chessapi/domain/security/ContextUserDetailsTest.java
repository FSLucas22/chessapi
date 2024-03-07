package com.lucas.chessapi.domain.security;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.security.userdetails.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextUserDetailsTest extends TestContextHelper {
    private UserDetailsImpl userDetails;
    private UserEntity user;

    protected void givenUser(UserEntity user) {
        this.user = user;
    }

    protected void whenUserDetailsIsCreated() {
        this.userDetails = new UserDetailsImpl(user);
    }

    protected void thenUserDetailsAuthoritiesShouldBe(Collection<GrantedAuthority> authorities) {
        assertThat(userDetails.getAuthorities()).isEqualTo(authorities);
    }

    protected void thenUserDetailsUsernameShouldBe(String username) {
        assertThat(userDetails.getUsername()).isEqualTo(username);
    }

    protected void thenUserDetailsPasswordShouldBe(String password) {
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    protected void thenUserDetailsIsAccountNonExpiredShouldBe(Boolean isAccountNonExpired) {
        assertThat(userDetails.isAccountNonExpired()).isEqualTo(isAccountNonExpired);
    }

    protected void thenUserDetailsIsCredentialsNonExpiredShouldBe(Boolean isCredentialsNonExpired) {
        assertThat(userDetails.isCredentialsNonExpired()).isEqualTo(isCredentialsNonExpired);
    }

    protected void thenUserDetailsIsEnabledShouldBe(Boolean isEnabled) {
        assertThat(userDetails.isEnabled()).isEqualTo(isEnabled);
    }

    protected void thenUserDetailsIsAccountNonLockedShouldBe(Boolean isAccountNonLocked) {
        assertThat(userDetails.isAccountNonLocked()).isEqualTo(isAccountNonLocked);
    }
}
