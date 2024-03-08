package com.lucas.chessapi.repository;

import com.lucas.chessapi.model.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
}
