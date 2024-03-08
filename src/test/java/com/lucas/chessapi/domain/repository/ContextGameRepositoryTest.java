package com.lucas.chessapi.domain.repository;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ContextGameRepositoryTest extends TestContextHelper {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Optional<GameEntity> result;

    protected void given(UserEntity... users) {
        Arrays.stream(users).forEach(entityManager::persist);
    }

    protected void given(GameEntity game) {
        entityManager.persist(game);
    }

    protected void whenFindById(Long id) {
        try {
            result = gameRepository.findById(id);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void whenSave(GameEntity game) {
        try {
            gameRepository.save(game);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResultShouldMatch(Optional<GameEntity> possibleGame) {
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(possibleGame);
    }
}
