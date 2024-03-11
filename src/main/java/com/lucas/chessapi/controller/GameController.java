package com.lucas.chessapi.controller;

import com.lucas.chessapi.dto.response.GetGameResponseDto;
import org.springframework.http.ResponseEntity;

public interface GameController {
    ResponseEntity<GetGameResponseDto> getGameById(Long id);
}
