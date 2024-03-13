package com.lucas.chessapi.controller;

import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<CreateUserResponseDto> create(CreateUserRequestDto request);

    ResponseEntity<GetAllUsersResponseDto> getAll(GetAllUsersRequestDto request);

    ResponseEntity<GetUserResponseDto> getAll(Long id);
}
