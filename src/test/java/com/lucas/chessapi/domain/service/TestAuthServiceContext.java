package com.lucas.chessapi.domain.service;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.JwtCreator;
import com.lucas.chessapi.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestAuthServiceContext extends TestContextHelper {

    @Mock
    UserRepository repository;

    @Mock
    JwtCreator jwtCreator;

    @InjectMocks
    AuthServiceImpl service;

    private AuthResponseDto response;

    protected void givenUserExists(UserEntity user) {
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    protected void givenUserRepositoryReturnsEmpty() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
    }

    protected void givenReturnedTokenIs(String token) {
        when(jwtCreator.issueToken(anyString(), any())).thenReturn(token);
    }

    protected void whenAuthenticationHappensFor(AuthRequestDto request) {
        try {
            this.response = service.authenticate(request);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResponseShouldBe(AuthResponseDto response) {
        assertThat(this.response)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    protected void thenShouldConsultReposityWithEmail(String email) {
        verify(repository, times(1)).findByEmail(email);
    }

    protected void thenShouldIssueToken() {
        verify(jwtCreator, times(1)).issueToken(anyString(), any());
    }

    protected void thenShouldNotIssueToken() {
        verify(jwtCreator, never()).issueToken(anyString(), any());
    }
}
