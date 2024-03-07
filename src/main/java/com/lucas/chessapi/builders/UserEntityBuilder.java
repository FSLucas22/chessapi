package com.lucas.chessapi.builders;

import com.lucas.chessapi.model.UserEntity;

import java.time.LocalDate;

public class UserEntityBuilder {
    UserEntity entity = new UserEntity();

    public static UserEntityBuilder getBuilder() {
        return new UserEntityBuilder();
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
}
