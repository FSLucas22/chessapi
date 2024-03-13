package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;
import com.lucas.chessapi.exceptions.UserCreationException;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.impl.CreateUserServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private CreateUserServiceImpl service;

    private CreateUserRequestDto request;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequestDto(
                "testuser",
                "test@email.com",
                "test123"
        );
    }

    @Test
    void shouldCreateValidUser() {
        when(repository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(
                UserEntityBuilderExtension
                        .valid()
                        .id(1L)
                        .username("testuser")
                        .email("test@email.com")
                        .build()
        );
        when(encoder.encode(any())).thenReturn(generateRandomString());
        var response = service.create(request);
        verify(encoder, times(1)).encode("test123");
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(new CreateUserResponseDto(
                        1L,
                        "testuser",
                        "test@email.com"
                ));
    }

    @Test
    void shouldThrowUserCreationExceptionWhenEmailAlreadyExists() {
        when(repository.findByEmail(request.email())).thenReturn(Optional.of(new UserEntity()));

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(UserCreationException.class)
                .hasMessage("Email already exists");

        verify(encoder, never()).encode(any());
        verify(repository, never()).save(any());
    }
}
