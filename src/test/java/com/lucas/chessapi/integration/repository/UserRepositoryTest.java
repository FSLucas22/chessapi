package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.repository.ContextUserRepositoryTest;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
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

    @Test
    void shouldReturnUsersSortedByName() {
        var bob = userBuilder.username("bob").email("bob@email.com").build();
        var alice = userBuilder.username("alice").email("alice@email.com").build();
        var sort = Sort.by(Sort.Order.asc("username"));
        var pagination = PageRequest.of(0, 2, sort);
        var expectedPage = new PageImpl<>(List.of(alice, bob), pagination, 2);
        givenUsers(bob, alice);
        whenFindAllIsCalledWith(pagination);
        thenShouldHaveNoErrors();

        thenReturnedPageShoudBe(expectedPage);
    }

    @Test
    void shouldNotThrowErrorOnPageWithNoUsers() {
        var sort = Sort.by(Sort.Order.asc("username"));
        var pagination = PageRequest.of(5, 2, sort);
        Page<UserEntity> expectedPage = new PageImpl<>(List.of(), pagination, 0);
        whenFindAllIsCalledWith(pagination);
        thenShouldHaveNoErrors();
        thenReturnedPageShoudBe(expectedPage);
    }
}
