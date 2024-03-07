package com.lucas.chessapi.controller.impl;

import com.lucas.chessapi.controller.UserController;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import com.lucas.chessapi.service.UserCreationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/chessapi/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserCreationService service;

    @Override
    @PostMapping
    public ResponseEntity<UserCreationResponseDto> create(
            @RequestBody @Valid UserCreationRequestDto request
    ) {
        var response = service.create(request);
        return ResponseEntity
                .created(URI.create("/chessapi/user/" + response.id()))
                .body(response);
    }
}
