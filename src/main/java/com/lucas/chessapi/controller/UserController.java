package com.lucas.chessapi.controller;

import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserController {
    @PostMapping
    ResponseEntity<UserCreationResponseDto> create(@RequestBody UserCreationRequestDto request);
}
