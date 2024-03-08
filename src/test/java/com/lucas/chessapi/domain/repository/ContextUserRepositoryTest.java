package com.lucas.chessapi.domain.repository;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContextUserRepositoryTest extends TestContextHelper {
    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private Optional<UserEntity> returnedUser;
    private Page<UserEntity> returnedUserPage;

    protected void givenUser(UserEntity user) {
        entityManager.persist(user);
    }

    protected void givenUsers(UserEntity... users) {
        Arrays.stream(users).forEach(entityManager::persist);
    }

    protected void whenFindByEmail(String email) {
        returnedUser = repository.findByEmail(email);
    }

    protected void whenUserIsSaved(UserEntity user) {
        try {
            repository.save(user);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void whenFindAllIsCalledWith(PageRequest pagination) {
        try {
            returnedUserPage = repository.findAll(pagination);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenReturnedUserShouldBeEqual(Optional<UserEntity> possibleUser) {
        assertThat(returnedUser)
                .usingRecursiveComparison()
                .isEqualTo(possibleUser);
    }

    protected void thenReturnedPageShoudBe(Page<UserEntity> userPage) {
        assertThat(returnedUserPage)
                .usingRecursiveComparison()
                .isEqualTo(userPage);
    }
}
