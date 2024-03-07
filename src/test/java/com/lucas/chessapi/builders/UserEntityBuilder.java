package com.lucas.chessapi.builders;

import com.lucas.chessapi.model.UserEntity;

import java.time.LocalDate;

public class UserEntityBuilder {
    UserEntity entity = new UserEntity();

    public static UserEntityBuilder valid() {
        return UserEntityBuilder.getBuilder()
                .id(1L)
                .email("user@test.com")
                .password("test123")
                .username("testuser");
    }

    public UserEntityBuilder username(String username) {
        entity.setUsername(username);
        return this;
    }

    public UserEntityBuilder password(String password) {
        entity.setPassword(password);
        return this;
    }

    public UserEntityBuilder email(String email) {
        entity.setEmail(email);
        return this;
    }

    public UserEntityBuilder id(Long id) {
        entity.setId(id);
        return this;
    }

    public static UserEntityBuilder getBuilder() {
        return new UserEntityBuilder();
    }

    public static UserEntity validUserEntity() {
        return validUserEntityWithId(1L);
    }

    public static UserEntity validUserEntityWithId(Long id) {
        return UserEntityBuilder.getBuilder()
                .id(id)
                .username("test")
                .email("email@test.com")
                .password("testpass")
                .build();
    }

    public UserEntity build() {
        return entity;
    }

    public UserEntityBuilder createdNow() {
        var now = LocalDate.now();
        return this.registeredAt(now).updatedAt(now);
    }

    public UserEntityBuilder updatedAt(LocalDate updatedDate) {
        entity.setUpdatedAt(updatedDate);
        return this;
    }

    public UserEntityBuilder registeredAt(LocalDate registeredDate) {
        entity.setRegisteredAt(registeredDate);
        return this;
    }
}
