package com.lucas.chessapi.configuration;

import com.lucas.chessapi.dto.game.PlayerDto;
import com.lucas.chessapi.game.OrderedPairFactory;
import com.lucas.chessapi.game.RandomElementSelector;
import com.lucas.chessapi.game.enums.OrderedPairCreationType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class GameConfiguration {
    public static final Long WAITING_TIME_MILLIS = 20L * 1000;

    @Bean
    public OrderedPairFactory<PlayerDto> orderedPairFactory(
            RandomElementSelector<OrderedPairCreationType> randomElementSelector
    ) {
        return new OrderedPairFactory<>(randomElementSelector);
    }

    @Bean
    public RandomElementSelector<OrderedPairCreationType> randomElementSelector() {
        return new RandomElementSelector<>(new Random());
    }
}
