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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
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

    private Slice<GameEntity> gamesSlice;

    @SafeVarargs
    protected final <T> void given(T... entities) {
        Arrays.stream(entities).forEach(entityManager::persist);
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

    protected void whenGetAllByUser(UserEntity player, PageRequest pagination) {
        try {
            gamesSlice = gameRepository.findAllByUser(player, pagination);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenResultShouldMatch(Optional<GameEntity> possibleGame) {
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(possibleGame);
    }

    protected void thenShouldHaveMorePages() {
        assertThat(gamesSlice.hasNext()).isTrue();
    }

    protected void thenGamesShouldMatch(List<GameEntity> games) {
        assertThat(gamesSlice.getContent())
                .usingRecursiveComparison()
                .isEqualTo(games);
    }
}
