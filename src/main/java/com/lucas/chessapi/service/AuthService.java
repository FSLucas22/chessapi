package com.lucas.chessapi.service;

import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;

public interface AuthService {
    AuthResponseDto authenticate(AuthRequestDto request);
}
