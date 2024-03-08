package com.lucas.chessapi.builders;

import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;

import java.time.LocalDateTime;

public class GameEntityBuilder {
    private Long id;
    private UserEntity firstPlayer;
    private UserEntity secondPlayer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static GameEntityBuilder getBuilder() {
        return new GameEntityBuilder();
    }

    public static GameEntityBuilder valid() {
        return GameEntityBuilder
                .getBuilder()
                .id(1L)
                .firstPlayer(UserEntityBuilderExtension
                        .valid()
                        .id(1L)
                        .username("first")
                        .email("first@email.com")
                        .build())
                .secondPlayer(UserEntityBuilderExtension
                        .valid()
                        .id(2L)
                        .username("second")
                        .email("second@email.com")
                        .build())
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

    public GameEntityBuilder createdAt(LocalDateTime date) {
        this.createdAt = date;
        return this;
    }

    public GameEntityBuilder updatedAt(LocalDateTime date) {
        this.updatedAt = date;
        return this;
    }

    public GameEntity build() {
        return new GameEntity(id, firstPlayer, secondPlayer, createdAt, updatedAt);
    }
}
