package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;

public interface GetGameService {
    GetGameResponseDto getById(Long id);

    GetAllGamesResponseDto getAllByUserId(Long id, Integer pageNumber, Integer pageSize);
}
