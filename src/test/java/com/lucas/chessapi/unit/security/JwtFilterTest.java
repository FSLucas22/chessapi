package com.lucas.chessapi.unit.security;

import com.lucas.chessapi.builders.UserEntityBuilderExtension;
import com.lucas.chessapi.configuration.SecurityConfiguration;
import com.lucas.chessapi.exceptions.InvalidTokenException;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.JwtFilter;
import com.lucas.chessapi.security.jwt.JwtTokenValidator;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static com.lucas.chessapi.builders.JwtTokenDtoFactory.jwtTokenDtoWithSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {
    @Mock
    UserRepository repository;
    @Mock
    TokenProcessor tokenProcessor;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;
    @Mock
    JwtTokenValidator validator;
    @Mock
    SecurityConfiguration configuration;
    @InjectMocks
    JwtFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetUserAuthenticationWhenTokenIsValid() throws ServletException, IOException {
        // GIVEN
        var validToken = "1234";
        var user = UserEntityBuilderExtension.validUserEntity();

        when(configuration.getPrefix()).thenReturn("Bearer");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        doNothing().when(validator).validate(validToken);
        when(tokenProcessor.getJwtTokenDtoFromToken(validToken))
                .thenReturn(jwtTokenDtoWithSubject(user.getId().toString()));
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(filterChain).doFilter(request, response);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .isInstanceOf(UserEntity.class)
                .usingRecursiveComparison()
                .isEqualTo(user);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(validator, times(1)).validate(validToken);
    }

    @Test
    void shouldPropagateErrorThrownByValidator() {
        // GIVEN
        var invalidToken = "1234";
        when(configuration.getPrefix()).thenReturn("Bearer");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        doThrow(new InvalidTokenException("test")).when(validator).validate(invalidToken);

        // WHEN
        assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
                // THEN
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("test");

        verify(validator, times(1)).validate(invalidToken);
        verify(tokenProcessor, never()).getJwtTokenDtoFromToken(anyString());
        verify(repository, never()).findById(anyLong());
    }

    @Test
    void shouldThrowInvalidTokenExceptionWhenSubjectDontMatchAUserId() {
        // GIVEN
        var invalidToken = "1234";
        when(configuration.getPrefix()).thenReturn("Bearer");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidToken);
        doNothing().when(validator).validate(invalidToken);
        when(tokenProcessor.getJwtTokenDtoFromToken(invalidToken))
                .thenReturn(jwtTokenDtoWithSubject("1"));
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // WHEN
        assertThatThrownBy(() -> filter.doFilter(request, response, filterChain))
                // THEN
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldNotSaveUserInContextWhenHeaderIsNull() throws ServletException, IOException {
        //GIVEN
        when(request.getHeader("Authorization")).thenReturn(null);

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        verify(validator, never()).validate(anyString());
        verify(tokenProcessor, never()).getJwtTokenDtoFromToken(anyString());
        verify(repository, never()).findById(anyLong());
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }

    @Test
    void shouldNotSaveUserInContextWhenHeaderDontHavePrefix() throws ServletException, IOException {
        // GIVEN
        when(configuration.getPrefix()).thenReturn("Bearer");
        when(request.getHeader("Authorization")).thenReturn("Something");

        // WHEN
        filter.doFilter(request, response, filterChain);

        // THEN
        verify(validator, never()).validate(anyString());
        verify(tokenProcessor, never()).getJwtTokenDtoFromToken(anyString());
        verify(repository, never()).findById(anyLong());
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }
}
