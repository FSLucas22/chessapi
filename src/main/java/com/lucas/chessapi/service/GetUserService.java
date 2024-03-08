package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;

public interface GetUserService {
    GetUserResponseDto getById(Long id);

    GetAllUsersResponseDto getAll(GetAllUsersRequestDto request);
}
