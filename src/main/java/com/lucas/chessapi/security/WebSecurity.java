package com.lucas.chessapi.security;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.JwtFilter;
import com.lucas.chessapi.security.jwt.JwtTokenValidator;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtFilter jwtFilter
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/chessapi/auth").permitAll()
                        .requestMatchers(HttpMethod.POST, "/chessapi/user").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenProcessor processor(SecurityConfiguration configuration) {
        return new TokenProcessor(configuration);
    }

    @Bean
    public JwtFilter jwtFilter(
            TokenProcessor processor,
            UserRepository repository,
            SecurityConfiguration configuration
    ) {
        var validator = new JwtTokenValidator(Jwts.parser().setSigningKey(configuration.getKey()));
        return new JwtFilter(processor, repository, validator, configuration);
    }
}
