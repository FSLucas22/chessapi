package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.domain.repository.ContextUserRepositoryTest;
import org.junit.jupiter.api.Test;
import java.util.Optional;


public class TestUserRepository extends ContextUserRepositoryTest {

    @Test
    void shouldFindUserByEmail() {
        var user = UserEntityBuilder
                .getBuilder()
                .username("testuser")
                .email("test@email.com")
                .password("1234")
                .createdNow()
                .build();
        givenUser(user);
        whenFindByEmail("test@email.com");
        thenReturnedUserShouldBeEqual(Optional.of(user));
    }

    @Test
    void shouldReturnEmptyWhenEmailIsNotFound() {
        whenFindByEmail("test@email.com");
        thenReturnedUserShouldBeEqual(Optional.empty());
    }
}
