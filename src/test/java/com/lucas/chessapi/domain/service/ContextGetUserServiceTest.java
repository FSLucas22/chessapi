package com.lucas.chessapi.domain.service;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.impl.GetUserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContextGetUserServiceTest extends TestContextHelper {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private GetUserServiceImpl service;
    private GetUserResponseDto result;

    protected void givenUserExists(UserEntity user) {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    protected void whenGetByIdIsCalledWith(Long id) {
        try {
            result = service.getById(id);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResultShouldBe(GetUserResponseDto response) {
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }
}
