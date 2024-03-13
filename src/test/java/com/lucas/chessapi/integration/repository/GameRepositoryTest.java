package com.lucas.chessapi.integration.repository;

import com.lucas.chessapi.builders.GameEntityBuilder;
import com.lucas.chessapi.domain.RepositoryContextHelper;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.GameRepository;
import com.lucas.chessapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameRepositoryTest extends RepositoryContextHelper {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldReturnUserDataInEntity() {
        // GIVEN
        var player = user(null, "player");
        var adversary = user(null, "adversary");
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(player)
                .secondPlayer(adversary)
                .build();

        given(player, adversary);
        given(game);

        // WHEN
        var result = gameRepository.findById(game.getId());

        // THEN
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(game));
    }

    @Test
    void shouldNotSaveWithUserThatDontExist() {
        // GIVEN
        var game = GameEntityBuilder
                .valid()
                .id(null)
                .firstPlayer(user(-1L, "player"))
                .secondPlayer(user(-2L, "adversary"))
                .build();

        // THEN
        assertThrows(Exception.class, () -> gameRepository.save(game));
    }

    @Test
    void shouldNotSaveWithNullUser() {
        // GIVEN
        var game = GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(null)
                .secondPlayer(null)
                .build();

        // THEN
        assertThrows(Exception.class, () -> gameRepository.save(game));
    }

    @Test
    void shouldReturnAllGamesWithUser() {
        // GIVEN
        var player = user(null, "player");
        var adversary = user(null, "adversary");
        var gameList = List.of(
                game(adversary, player),
                game(player, adversary),
                game(player, adversary),
                game(player, adversary)
        );
        var pagination = PageRequest.of(0, 3, Sort.by(Sort.Order.desc("updatedAt")));

        given(player, adversary);
        given(gameList.toArray(new GameEntity[4]));

        gameList = gameList.stream()
                .sorted(Comparator.comparing(GameEntity::getUpdatedAt).reversed())
                .limit(3)
                .toList();

        // WHEN
        var gamesSlice = gameRepository.findAllByUser(player, pagination);

        // THEN
        assertThat(gamesSlice.getContent())
                .usingRecursiveComparison()
                .isEqualTo(gameList);
        assertThat(gamesSlice.hasNext()).isTrue();
    }

    private GameEntity game(UserEntity firstPlayer, UserEntity secondPlayer) {
        return GameEntityBuilder.valid()
                .id(null)
                .firstPlayer(firstPlayer)
                .secondPlayer(secondPlayer)
                .build();
    }
}
