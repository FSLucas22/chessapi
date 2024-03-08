package com.lucas.chessapi.model;

import jakarta.persistence.*;
import lombok.*;

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
}
