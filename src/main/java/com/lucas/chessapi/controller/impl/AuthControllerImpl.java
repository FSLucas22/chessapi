package com.lucas.chessapi.controller.impl;

import com.lucas.chessapi.controller.AuthController;
import com.lucas.chessapi.dto.request.AuthRequestDto;
import com.lucas.chessapi.dto.response.AuthResponseDto;
import com.lucas.chessapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chessapi/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final AuthService service;

    @PostMapping
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
