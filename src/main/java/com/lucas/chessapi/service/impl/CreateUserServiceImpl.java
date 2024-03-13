package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.dto.request.CreateUserRequestDto;
import com.lucas.chessapi.dto.response.CreateUserResponseDto;
import com.lucas.chessapi.exceptions.UserCreationException;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserServiceImpl implements CreateUserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public CreateUserResponseDto create(CreateUserRequestDto request) {
        if (repository.findByEmail(request.email()).isPresent()) {
            throw new UserCreationException("Email already exists");
        }
        UserEntity user = request.toUserEntity(encoder);
        var savedUser = repository.save(user);
        return new CreateUserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }
}
