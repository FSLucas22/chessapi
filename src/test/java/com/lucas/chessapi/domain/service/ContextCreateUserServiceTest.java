package com.lucas.chessapi.domain.service;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.impl.UserCreationServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContextCreateUserServiceTest extends TestContextHelper {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserCreationServiceImpl service;

    private UserCreationResponseDto response;

    protected void givenUserDontExistFor(String email) {
        when(repository.findByEmail(email)).thenReturn(Optional.empty());
    }

    protected void givenUserExistsFor(String email) {
        when(repository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));
    }

    protected void givenRepositoryReturnsOnSave(UserEntity user) {
        when(repository.save(any())).thenReturn(user);
    }

    protected void givenEncodedPasswordIs(String encodedPassword) {
        when(encoder.encode(any())).thenReturn(encodedPassword);
    }

    protected void whenUserIsCreated(UserCreationRequestDto request) {
        try {
            response = service.create(request);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenPasswordMustHaveBeenEncoded(String password) {
        verify(encoder, times(1)).encode(password);
    }

    protected void thenResponseShouldBe(UserCreationResponseDto response) {
        assertThat(this.response)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    protected void thenShouldNotEncodePassword() {
        verify(encoder, never()).encode(any());
    }

    protected void thenShouldNotSaveUser() {
        verify(repository, never()).save(any());
    }
}
