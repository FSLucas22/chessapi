package com.lucas.chessapi.controller;

import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<UserCreationResponseDto> create(UserCreationRequestDto request);

    ResponseEntity<GetAllUsersResponseDto> getAll(GetAllUsersRequestDto request);

    ResponseEntity<GetUserResponseDto> getAll(Long id);
}
