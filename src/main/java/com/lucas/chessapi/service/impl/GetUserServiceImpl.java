package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.exceptions.GetUserException;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.service.GetUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserServiceImpl implements GetUserService {
    private final UserRepository repository;

    @Override
    public GetUserResponseDto getById(Long id) {
        var user = repository.findById(id).orElseThrow(() -> new GetUserException("User not found"));
        return new GetUserResponseDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
