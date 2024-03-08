package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.repository.ContextGameRepositoryTest;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class GameRepositoryTest extends ContextGameRepositoryTest {
    private UserEntityBuilder builder;

    @BeforeEach
    void setUp() {
        builder = UserEntityBuilderExtension
                .valid()
                .id(null)
                .email("test@email.com");
    }

    @Test
    void shouldReturnUserDataInEntity() {
        var player = user("player");
        var adversary = user("adversary");
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(player)
                .secondPlayer(adversary)
                .build();
        given(player, adversary);
        given(game);
        whenFindById(game.getId());
        thenShouldHaveNoErrors();
        thenResultShouldMatch(Optional.of(game));
    }

    @Test
    void shouldNotSaveWithUserThatDontExist() {
        var player = user("player");
        var adversary = user("adversary");
        var game = GameEntityBuilder
                .valid()
                .id(null)
                .firstPlayer(player)
                .secondPlayer(adversary)
                .build();

        whenSave(game);
        thenShouldThrow(Exception.class);
    }

    @Test
    void shouldNotSaveWithNullUser() {
        whenSave(GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(null)
                .secondPlayer(null)
                .build());
        thenShouldThrow(Exception.class);
    }

    private UserEntity user(String username) {
        return builder.username(username).email(username + "@email.com").build();
    }
}
