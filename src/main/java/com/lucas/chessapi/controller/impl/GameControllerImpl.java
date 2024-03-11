package com.lucas.chessapi.controller.impl;

import com.lucas.chessapi.dto.request.CreateGameRequestDto;
import com.lucas.chessapi.dto.response.CreateGameResponseDto;
import com.lucas.chessapi.dto.response.GetAllGamesResponseDto;
import com.lucas.chessapi.dto.response.GetGameResponseDto;
import com.lucas.chessapi.service.CreateGameService;
import com.lucas.chessapi.service.GetGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/chessapi/game")
@RequiredArgsConstructor
public class GameControllerImpl {
    private final GetGameService getGameService;
    private final CreateGameService createGameService;

    @Value("${security.config.prefix}")
    private String prefix;

    @GetMapping("/{id}")
    public ResponseEntity<GetGameResponseDto> getGameById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(getGameService.getById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GetAllGamesResponseDto> getAllUserGames(
            @PathVariable("id") Long id,
            @RequestParam("page") Integer pageNumber,
            @RequestParam("limit") Integer pageSize
    ) {
        return ResponseEntity.ok(getGameService.getAllByUserId(id, pageNumber, pageSize));
    }

    @PostMapping
    public ResponseEntity<CreateGameResponseDto> createGame(
            @Valid @RequestBody CreateGameRequestDto request,
            @RequestHeader("Authorization") String header
    ) {
        var token = extractTokenFromHeader(header);
        var result = createGameService.createGame(token, request);
        return ResponseEntity.created(URI.create("/chessapi/game/" + result.id())).body(result);
    }

    private String extractTokenFromHeader(String header) {
        return header.replace(prefix, "");
    }
}
