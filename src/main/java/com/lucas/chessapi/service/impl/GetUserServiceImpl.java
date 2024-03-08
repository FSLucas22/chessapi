package com.lucas.chessapi.service.impl;

import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
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

    @Override
    public GetAllUsersResponseDto getAll(GetAllUsersRequestDto request) {
        var pagination = request.generatePagination();
        return GetAllUsersResponseDto.from(repository.findAll(pagination));
    }
}
