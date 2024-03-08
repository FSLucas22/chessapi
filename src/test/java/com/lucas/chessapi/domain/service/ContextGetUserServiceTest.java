package com.lucas.chessapi.domain.service;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.impl.GetUserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
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
    private GetAllUsersResponseDto allUsersResult;

    protected void givenUserExists(UserEntity user) {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
    }

    protected void givenUserListIsReturned(List<UserEntity> users) {
        var page = PageRequest.of(
                0,
                users.size(),
                Sort.by(Sort.Order.asc("username"))
        );
        when(repository.findAll(page)).thenReturn(users);
    }

    protected void whenGetByIdIsCalledWith(Long id) {
        try {
            result = service.getById(id);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void whenGetAllIsCalledWith(GetAllUsersRequestDto request) {
        try {
            allUsersResult = service.getAll(request);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResultShouldBe(GetUserResponseDto response) {
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }

    protected void thenResultShouldBe(GetAllUsersResponseDto response) {
        assertThat(allUsersResult)
                .usingRecursiveComparison()
                .isEqualTo(response);
    }
}
