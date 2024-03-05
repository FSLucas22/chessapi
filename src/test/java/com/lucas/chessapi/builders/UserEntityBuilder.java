package com.lucas.chessapi.builders;

import com.lucas.chessapi.model.UserEntity;

public class UserEntityBuilder {
    UserEntity entity = new UserEntity();

    public static UserEntityBuilder getBuilder() {
        return new UserEntityBuilder();
    }

    public UserEntityBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public UserEntityBuilder username(String username) {
        entity.setUsername(username);
        return this;
    }

    public UserEntityBuilder email(String email) {
        entity.setEmail(email);
        return this;
    }

    public UserEntityBuilder password(String password) {
        entity.setPassword(password);
        return this;
    }

    public UserEntity build() {
        return entity;
    }

    public static UserEntity validUserEntity() {
        return UserEntityBuilder.getBuilder()
                .id(1L)
                .username("test")
                .email("email@test.com")
                .password("testpass")
                .build();
    }
}
