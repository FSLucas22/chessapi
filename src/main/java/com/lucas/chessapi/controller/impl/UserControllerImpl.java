package com.lucas.chessapi.controller.impl;

import com.lucas.chessapi.controller.UserController;
import com.lucas.chessapi.dto.request.GetAllUsersRequestDto;
import com.lucas.chessapi.dto.request.UserCreationRequestDto;
import com.lucas.chessapi.dto.response.GetAllUsersResponseDto;
import com.lucas.chessapi.dto.response.GetUserResponseDto;
import com.lucas.chessapi.dto.response.UserCreationResponseDto;
import com.lucas.chessapi.service.GetUserService;
import com.lucas.chessapi.service.UserCreationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/chessapi/user")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserCreationService createUserService;
    private final GetUserService getUserService;

    @Override
    @PostMapping
    public ResponseEntity<UserCreationResponseDto> create(
            @RequestBody @Valid UserCreationRequestDto request
    ) {
        var response = createUserService.create(request);
        return ResponseEntity
                .created(URI.create("/chessapi/user/" + response.id()))
                .body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<GetAllUsersResponseDto> getAll(@RequestBody GetAllUsersRequestDto request) {
        return ResponseEntity.ok(getUserService.getAll(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponseDto> getAll(@PathVariable("id") Long id) {
        return ResponseEntity.ok(getUserService.getById(id));
    }
}
