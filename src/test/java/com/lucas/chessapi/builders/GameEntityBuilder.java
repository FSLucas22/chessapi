package com.lucas.chessapi.builders;

import com.lucas.chessapi.configuration.GameConfiguration;
import com.lucas.chessapi.game.enums.GameStatus;
import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;

import java.time.LocalDateTime;

import static com.lucas.chessapi.builders.UserEntityBuilderExtension.user;

public class GameEntityBuilder {
    private Long id;
    private UserEntity firstPlayer;
    private UserEntity secondPlayer;
    private String moves;
    private Integer numberOfMoves;
    private GameStatus status;
    private Long firstPlayerRemainingTimeMillis;
    private Long secondPlayerRemainingTimeMillis;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GameEntityBuilder getBuilder() {
        return new GameEntityBuilder();
    }

    public static GameEntityBuilder valid() {
        return GameEntityBuilder
                .getBuilder()
                .id(1L)
                .firstPlayer(user(1L, "first"))
                .secondPlayer(user(2L, "second"))
                .moves("")
                .numberOfMoves(0)
                .status(GameStatus.WAITING_FIRST_PLAYER)
                .firstPlayerRemainingTimeMillis(GameConfiguration.WAITING_TIME_MILLIS)
                .secondPlayerRemainingTimeMillis(GameConfiguration.WAITING_TIME_MILLIS)
                .createdNow();
    }

    public GameEntityBuilder createdNow() {
        return createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now());
    }

    public GameEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public GameEntityBuilder firstPlayer(UserEntity user) {
        this.firstPlayer = user;
        return this;
    }

    public GameEntityBuilder secondPlayer(UserEntity user) {
        this.secondPlayer = user;
        return this;
    }

    public GameEntityBuilder moves(String moves) {
        this.moves = moves;
        return this;
    }

    public GameEntityBuilder numberOfMoves(Integer numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
        return this;
    }

    public GameEntityBuilder status(GameStatus status) {
        this.status = status;
        return this;
    }

    public GameEntityBuilder firstPlayerRemainingTimeMillis(Long firstPlayerRemainingTimeMillis) {
        this.firstPlayerRemainingTimeMillis = firstPlayerRemainingTimeMillis;
        return this;
    }

    public GameEntityBuilder secondPlayerRemainingTimeMillis(Long secondPlayerRemainingTimeMillis) {
        this.secondPlayerRemainingTimeMillis = secondPlayerRemainingTimeMillis;
        return this;
    }

    public GameEntityBuilder createdAt(LocalDateTime date) {
        this.createdAt = date;
        return this;
    }

    public GameEntityBuilder updatedAt(LocalDateTime date) {
        this.updatedAt = date;
        return this;
    }

    public GameEntity build() {
        return new GameEntity(
                id,
                firstPlayer,
                secondPlayer,
                moves,
                numberOfMoves,
                status,
                firstPlayerRemainingTimeMillis,
                secondPlayerRemainingTimeMillis,
                createdAt,
                updatedAt
        );
    }
}
