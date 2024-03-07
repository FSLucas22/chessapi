package com.lucas.chessapi.dto.response;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

public record ErrorsResponseDto(List<String> errors) {
    public static ErrorsResponseDto from(BindingResult errors) {
        var errorList = errors.getAllErrors()
                .stream()
                .map(ErrorsResponseDto::processObjectError)
                .toList();
        return new ErrorsResponseDto(errorList);
    }

    private static String processObjectError(ObjectError error) {
        var fieldError = (FieldError) error;
        return fieldError.getField() + " " + error.getDefaultMessage();
    }
}
