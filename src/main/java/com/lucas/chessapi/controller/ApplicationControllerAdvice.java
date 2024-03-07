package com.lucas.chessapi.controller;

import com.lucas.chessapi.dto.response.ErrorResponseDto;
import com.lucas.chessapi.exceptions.UserAuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationError(UserAuthenticationException error) {
        var response = new ErrorResponseDto(error.getMessage(), 400);
        return ResponseEntity.badRequest().body(response);
    }
}
