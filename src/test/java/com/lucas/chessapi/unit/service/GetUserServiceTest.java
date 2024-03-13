package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.impl.GetUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static com.lucas.chessapi.builders.UserEntityBuilderExtension.validUserEntityWithId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserServiceTest {
    @Mock
    private UserRepository repository;
    @InjectMocks
    private GetUserServiceImpl service;

    @Test
    void shouldReturnUserInformationById() {
        // GIVEN
        var user = validUserEntityWithId(1L);
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        // WHEN
        GetUserResponseDto result = service.getById(1L);

        // THEN
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetUserResponseDto(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Test
    void shouldThrowGetUserExceptionWhenUserDontExist() {
        // WHEN
        assertThatThrownBy(() -> service.getById(1L))
                // THEN
                .isInstanceOf(GetUserException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldGetAllUsersInPaginationSortedByName() {
        // GIVEN
        var request = new GetAllUsersRequestDto(0, 3, List.of("username"));
        var users = List.of(
                user(3L, "Alice"),
                user(2L, "Bob"),
                user(1L, "Cheryl")
        );

        var pageable = PageRequest.of(
                0,
                users.size(),
                Sort.by(Sort.Order.asc("username"))
        );
        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(users, pageable, users.size()));

        // WHEN
        var result = service.getAll(request);

        // THEN
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(new GetAllUsersResponseDto(List.of(
                        new GetUserResponseDto(3L, "Alice", "alice@email.com"),
                        new GetUserResponseDto(2L, "Bob", "bob@email.com"),
                        new GetUserResponseDto(1L, "Cheryl", "cheryl@email.com")
                )));
    }
}
