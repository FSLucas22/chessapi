package com.lucas.chessapi.dto.game;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.model.UserEntity;

public record PlayerDto(Long id, String username) {
    public static PlayerDto from(GetUserResponseDto user) {
        return new PlayerDto(user.id(), user.username());
    }

    public static PlayerDto from(UserEntity user) {
        return new PlayerDto(user.getId(), user.getUsername());
    }

    public UserEntity toUserEntity() {
        return UserEntityBuilder.getBuilder()
                .id(id)
                .username(username)
                .build();
    }
}
