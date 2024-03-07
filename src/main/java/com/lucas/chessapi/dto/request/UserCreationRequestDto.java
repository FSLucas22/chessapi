package com.lucas.chessapi.dto.request;

import com.lucas.chessapi.builders.UserEntityBuilder;
import com.lucas.chessapi.model.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

public record UserCreationRequestDto(String username, String email, String password) {
    public UserEntity toUserEntity(PasswordEncoder encoder) {
        return UserEntityBuilder.getBuilder()
                .username(username)
                .email(email)
                .password(encoder.encode(password))
                .createdNow()
                .build();
    }

}
