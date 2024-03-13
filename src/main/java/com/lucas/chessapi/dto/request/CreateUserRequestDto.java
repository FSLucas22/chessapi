package com.lucas.chessapi.dto.request;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.model.UserEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

public record CreateUserRequestDto(

        @NotBlank String username,
        @NotBlank String email,
        @NotBlank String password) {
    public static CreateUserRequestDto fromUserEntity(UserEntity user) {
        return new CreateUserRequestDto(user.getUsername(), user.getEmail(), user.getPassword());
    }

    public UserEntity toUserEntity(PasswordEncoder encoder) {
        return UserEntityBuilder.getBuilder()
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .createdNow()
                .build();
    }
}
