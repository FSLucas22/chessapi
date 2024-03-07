package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.repository.ContextUserRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;


public class UserRepositoryTest extends ContextUserRepositoryTest {
    private UserEntityBuilder userBuilder;

    @BeforeEach
    void setUp() {
        userBuilder = UserEntityBuilderExtension
                .valid()
                .id(null)
                .email("test@email.com");
    }

    @Test
    void shouldFindUserByEmail() {
        var user = userBuilder.build();
        givenUser(user);
        whenFindByEmail("test@email.com");
        thenReturnedUserShouldBeEqual(Optional.of(user));
    }

    @Test
    void shouldReturnEmptyWhenEmailIsNotFound() {
        whenFindByEmail("test@email.com");
        thenReturnedUserShouldBeEqual(Optional.empty());
    }

    @Test
    void shouldNotCreateUserWithRepeatedEmail() {
        givenUser(userBuilder.build());
        whenUserIsSaved(userBuilder.build());
        thenShouldThrowContaining(
                DataIntegrityViolationException.class,
                "Unique index or primary key violation",
                "test@email.com"
        );
    }
}
