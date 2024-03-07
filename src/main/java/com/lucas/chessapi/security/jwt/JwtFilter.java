package com.lucas.chessapi.security.jwt;

import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.exceptions.InvalidTokenException;
import com.lucas.chessapi.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProcessor tokenProcessor;
    private final UserRepository repository;
    private final JwtTokenValidator validator;
    private final SecurityConfiguration securityConfiguration;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        var header = request.getHeader("Authorization");
        if (header != null && header.startsWith(getPrefix())) {
            var authenticationToken = getAuthenticationToken(header);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    private String getPrefix() {
        return securityConfiguration.getPrefix() + " ";
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String header) {
        var token = extractToken(header);
        validator.validate(token);
        var dto = tokenProcessor.getJwtTokenDtoFromToken(token);
        var userId = Long.parseLong(dto.subject());
        var user = repository.findById(userId)
                .orElseThrow(() -> new InvalidTokenException("User not found"));

        return new UsernamePasswordAuthenticationToken(user, null, List.of());
    }

    private String extractToken(String header) {
        return header.replace(getPrefix(), "");
    }
}
