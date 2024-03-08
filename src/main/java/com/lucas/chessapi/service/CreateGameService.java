package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;

public interface CreateGameService {
    CreateGameResponseDto createGame(String token, CreateGameRequestDto request);
}
