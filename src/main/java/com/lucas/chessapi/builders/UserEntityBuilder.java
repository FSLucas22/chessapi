package com.lucas.chessapi.builders;

import com.lucas.chessapi.model.UserEntity;

import java.time.LocalDateTime;

public class UserEntityBuilder {
    private Long id;
    private String username;
    private String email;
    private String password;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;

    public static UserEntityBuilder getBuilder() {
        return new UserEntityBuilder();
    }

    public UserEntity build() {
        return new UserEntity(id, username, email, password, registeredAt, updatedAt);
    }


    public UserEntityBuilder createdNow() {
        var now = LocalDateTime.now();
        return this.registeredAt(now).updatedAt(now);
    }

    public UserEntityBuilder updatedAt(LocalDateTime updatedDate) {
        this.updatedAt = updatedDate;
        return this;
    }

    public UserEntityBuilder registeredAt(LocalDateTime registeredDate) {
        this.registeredAt = registeredDate;
        return this;
    }

    public UserEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserEntityBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserEntityBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserEntityBuilder password(String password) {
        this.password = password;
        return this;
    }
}
