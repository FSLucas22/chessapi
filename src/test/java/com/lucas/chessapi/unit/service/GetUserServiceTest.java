package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.service.ContextGetUserServiceTest;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.validUserEntityWithId;

public class GetUserServiceTest extends ContextGetUserServiceTest {

    @Test
    void shouldReturnUserInformationById() {
        var user = validUserEntityWithId(1L);
        givenUserExists(user);
        whenGetByIdIsCalledWith(1L);
        thenShouldHaveNoErrors();
        thenResultShouldBe(new GetUserResponseDto(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Test
    void shouldThrowGetUserExceptionWhenUserDontExist() {
        whenGetByIdIsCalledWith(1L);
        thenShouldThrow(GetUserException.class, "User not found");
    }

    @Test
    void shouldGetAllUsersInPaginationSortedByName() {
        var request = new GetAllUsersRequestDto(0, 3, List.of("username"));
        givenUserListIsReturned(List.of(
                user(3L, "Alice"),
                user(2L, "Bob"),
                user(1L, "Cheryl")
        ));
        whenGetAllIsCalledWith(request);
        thenShouldHaveNoErrors();
        thenResultShouldBe(new GetAllUsersResponseDto(List.of(
                new GetUserResponseDto(3L, "Alice", "alice@email.com"),
                new GetUserResponseDto(2L, "Bob", "bob@email.com"),
                new GetUserResponseDto(1L, "Cheryl", "cheryl@email.com")
        )));
    }

    private UserEntity user(Long id, String username) {
        return UserEntityBuilderExtension
                .valid()
                .id(id)
                .username(username)
                .email(username.toLowerCase() + "@email.com")
                .build();
    }
}
