package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.response.GetUserResponseDto;

public interface GetUserService {
    GetUserResponseDto getById(Long id);
}
