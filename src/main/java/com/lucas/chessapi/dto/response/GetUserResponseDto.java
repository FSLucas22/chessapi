package com.lucas.chessapi.dto.response;

import com.lucas.chessapi.model.UserEntity;

public record GetUserResponseDto(Long id, String username, String email) {
    public static GetUserResponseDto from(UserEntity user) {
        return new GetUserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
