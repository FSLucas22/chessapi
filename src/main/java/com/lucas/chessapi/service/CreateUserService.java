package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;

public interface CreateUserService {
    CreateUserResponseDto create(CreateUserRequestDto request);
}
