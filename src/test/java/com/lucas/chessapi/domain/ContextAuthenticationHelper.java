package com.lucas.chessapi.domain;

import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.lucas.chessapi.builders.DateFactory.today;

@Component
public class ContextAuthenticationHelper {
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenProcessor tokenProcessor;
    @Value("${security.config.prefix}")
    private String prefix;
    private String token;

    public void givenAuthenticationHappensFor(UserEntity user) {
        token = tokenProcessor.issueToken(user.getId().toString(), today());
    }

    public void clean() {
        token = null;
    }

    public String getToken() {
        return token;
    }

    public String getHeader() {
        if (token == null) {
            return "";
        }
        return prefix + " " + token;
    }
}
