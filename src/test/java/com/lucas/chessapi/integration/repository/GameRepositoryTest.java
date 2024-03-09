package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.domain.repository.ContextGameRepositoryTest;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
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

    @Test
    void shouldReturnAllGamesWithUser() {
        var player = user("player");
        var adversary = user("adversary");
        var game1 = game(adversary, player);
        var game2 = game(player, adversary);
        var game3 = game(player, adversary);
        var game4 = game(player, adversary);
        var pagination = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("updatedAt")));

        given(player, adversary);
        given(game1, game2, game3, game4);
        whenGetAllByUser(player, pagination);
        thenShouldHaveNoErrors();
        thenGamesShouldMatch(List.of(game4, game3, game2));
        thenShouldHaveMorePages();
    }

    private UserEntity user(String username) {
        return builder.username(username).email(username + "@email.com").build();
    }

    private GameEntity game(UserEntity firstPlayer, UserEntity secondPlayer) {
        return GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
    }
}
