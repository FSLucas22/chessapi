package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;

public interface CreateUserService {
    UserCreationResponseDto create(UserCreationRequestDto request);
}
