package com.lucas.chessapi.domain.security;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.userdetails.UserDetailsImpl;
import com.lucas.chessapi.security.userdetails.UserDetailsServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContextUserDetailsServiceTest extends TestContextHelper {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private UserDetails userDetails;

    protected void givenUserExists(UserEntity user) {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    protected void whenLoadByUsernameIsCalledWith(String email) {
        try {
            this.userDetails = userDetailsService.loadUserByUsername(email);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenShouldReturn(UserDetailsImpl userDetails) {
        assertThat(this.userDetails)
                .usingRecursiveComparison()
                .isEqualTo(userDetails);
    }
}
