package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;
import com.lucas.chessapi.exceptions.UserAuthenticationException;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import com.lucas.chessapi.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.lucas.chessapi.Utils.generateRandomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    PasswordEncoder encoder;
    @Mock
    UserRepository repository;
    @Mock
    TokenProcessor tokenProcessor;
    @InjectMocks
    AuthServiceImpl service;

    AuthRequestDto request;
    UserEntity user;

    @BeforeEach
    void setUp() {
        request = new AuthRequestDto("test@email.com", "test123");
        var encryptedPassword = generateRandomString();

        user = UserEntityBuilderExtension
                .getBuilder()
                .id(1L)
                .email("test@email.com")
                .password(encryptedPassword)
                .username("testuser")
                .build();
    }

    @Test
    void shouldAuthenticateUserWithCorrectRequest() {
        // GIVEN
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(tokenProcessor.issueToken(anyString(), any())).thenReturn("1234");
        when(encoder.matches(request.password(), user.getPassword())).thenReturn(true);

        // WHEN
        var response = service.authenticate(request);

        // THEN
        verify(repository, times(1)).findByEmail("test@email.com");
        verify(tokenProcessor, times(1)).issueToken(anyString(), any());
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(new AuthResponseDto("testuser", "1234"));
    }

    @Test
    void shouldThrowUserAuthenticationExceptionWhenUserIsNotFound() {
        // GIVEN
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> service.authenticate(request))
                // THEN
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessage("Invalid email or password");

        verify(repository, times(1)).findByEmail("test@email.com");
        verify(tokenProcessor, never()).issueToken(anyString(), any());
    }

    @Test
    void shouldThrowUserAuthenticationExceptionWhenPasswordIsIncorrect() {
        // GIVEN
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword())).thenReturn(false);

        // WHEN
        assertThatThrownBy(() -> service.authenticate(request))
                // THEN
                .isInstanceOf(UserAuthenticationException.class)
                .hasMessage("Invalid email or password");

        verify(repository, times(1)).findByEmail("test@email.com");
        verify(tokenProcessor, never()).issueToken(anyString(), any());
    }
}
