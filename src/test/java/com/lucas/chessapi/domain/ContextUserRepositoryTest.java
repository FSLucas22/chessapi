package com.lucas.chessapi.domain;

import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContextUserRepositoryTest extends TestContextHelper{
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Optional<UserEntity> returnedUser;

    protected void givenUser(UserEntity user) {
        entityManager.persist(user);
    }

    protected void whenFindByEmail(String email) {
        returnedUser = repository.findByEmail(email);
    }

    protected void thenReturnedUserShouldBeEqual(Optional<UserEntity> possibleUser) {
        assertThat(returnedUser)
                .usingRecursiveComparison()
                .isEqualTo(possibleUser);
    }
}
