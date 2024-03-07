package com.lucas.chessapi.builders;

import com.lucas.chessapi.model.UserEntity;

public class UserEntityBuilderExtension extends UserEntityBuilder {
    public static UserEntity validUserEntity() {
        return validUserEntityWithId(1L);
    }

    public static UserEntity validUserEntityWithId(Long id) {
        return UserEntityBuilderExtension.valid().id(id).build();
    }

    public static UserEntityBuilder valid() {
        return UserEntityBuilder.getBuilder()
                .id(1L)
                .email("user@test.com")
                .password("test123")
                .username("testuser")
                .createdNow();
    }
}
