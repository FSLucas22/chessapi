package com.lucas.chessapi.unit.service;

import com.lucas.chessapi.domain.service.ContextGetUserServiceTest;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import org.junit.jupiter.api.Test;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.validUserEntityWithId;

public class GetGetUserServiceTest extends ContextGetUserServiceTest {

    @Test
    void shouldReturnUserInformationById() {
        var user = validUserEntityWithId(1L);
        givenUserExists(user);
        whenGetByIdIsCalledWith(1L);
        thenResultShouldBe(new GetUserResponseDto(user.getId(), user.getUsername(), user.getEmail()));
    }

    @Test
    void shouldThrowGetUserExceptionWhenUserDontExist() {
        whenGetByIdIsCalledWith(1L);
        thenShouldThrow(GetUserException.class, "User not found");
    }
}
