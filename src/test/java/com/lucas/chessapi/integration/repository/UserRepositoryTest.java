package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.RepositoryContextHelper;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class UserRepositoryTest extends RepositoryContextHelper {
    @Autowired
    private UserRepository repository;

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
        given(user);
        var possibleUser = repository.findByEmail("test@email.com");
        assertThat(possibleUser)
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(user));
    }

    @Test
    void shouldReturnEmptyWhenEmailIsNotFound() {
        var possibleUser = repository.findByEmail("test@email.com");
        assertThat(possibleUser)
                .usingRecursiveComparison()
                .isEqualTo(Optional.empty());
    }

    @Test
    void shouldNotCreateUserWithRepeatedEmail() {
        given(userBuilder.build());
        assertThatThrownBy(() -> repository.save(userBuilder.build()))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContainingAll(
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
        given(bob, alice);
        var page = repository.findAll(pagination);
        assertThat(page)
                .usingRecursiveComparison()
                .isEqualTo(expectedPage);
    }

    @Test
    void shouldNotThrowErrorOnPageWithNoUsers() {
        var sort = Sort.by(Sort.Order.asc("username"));
        var pagination = PageRequest.of(5, 2, sort);
        Page<UserEntity> expectedPage = new PageImpl<>(List.of(), pagination, 0);
        var page = repository.findAll(pagination);
        assertThat(page)
                .usingRecursiveComparison()
                .isEqualTo(expectedPage);
    }
}
