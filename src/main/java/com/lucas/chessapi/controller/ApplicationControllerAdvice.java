package com.lucas.chessapi.controller;

import com.lucas.chessapi.dto.response.ErrorResponseDto;
import com.lucas.chessapi.dto.response.ErrorsResponseDto;
import com.lucas.chessapi.exceptions.BusinessException;
import com.lucas.chessapi.exceptions.UserAuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApplicationControllerAdvice {
    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handle(UserAuthenticationException error) {
        var response = new ErrorResponseDto(error.getMessage(), 400);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorsResponseDto> handle(MethodArgumentNotValidException error) {
        return ResponseEntity.badRequest().body(ErrorsResponseDto.from(error.getBindingResult()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorsResponseDto> handle(BusinessException error) {
        return new ResponseEntity<>(
                new ErrorsResponseDto(List.of(error.getMessage())),
                error.getStatus()
        );
    }
}
