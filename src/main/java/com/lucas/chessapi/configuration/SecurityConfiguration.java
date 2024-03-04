package com.lucas.chessapi.configuration;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.config")
public record SecurityConfiguration(String prefix, String key, Long expiration) {
}
