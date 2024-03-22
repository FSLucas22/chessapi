package com.lucas.chessapi.model;

import com.lucas.chessapi.game.enums.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity firstPlayer;

    @ManyToOne
    @JoinColumn(nullable = false)
    private UserEntity secondPlayer;

    @Column(nullable = false)
    private String moves;

    @Column(nullable = false)
    private Integer numberOfMoves;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column(nullable = false)
    private Long firstPlayerRemainingTimeMillis;

    @Column(nullable = false)
    private Long secondPlayerRemainingTimeMillis;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt = LocalDateTime.now();
}
