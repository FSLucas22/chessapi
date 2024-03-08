package com.lucas.chessapi.dto.response;

import com.lucas.chessapi.model.UserEntity;

import java.util.List;

public record GetAllUsersResponseDto(List<GetUserResponseDto> users) {
    public static GetAllUsersResponseDto from(List<UserEntity> users) {
        var userDtos = users
                .stream()
                .map(GetUserResponseDto::from)
                .toList();

        return new GetAllUsersResponseDto(userDtos);
    }
}
