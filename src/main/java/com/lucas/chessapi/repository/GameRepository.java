package com.lucas.chessapi.repository;

import com.lucas.chessapi.model.GameEntity;
import com.lucas.chessapi.model.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
    @Query("""
            SELECT t FROM GameEntity t
            WHERE :user IN (t.firstPlayer, t.secondPlayer)
                        """)
    Slice<GameEntity> findAllByUser(UserEntity user, Pageable pagination);
}
