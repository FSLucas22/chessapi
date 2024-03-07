package com.lucas.chessapi.domain.security;

import com.lucas.chessapi.domain.TestContextHelper;
import com.lucas.chessapi.model.UserEntity;
import com.lucas.chessapi.repository.UserRepository;
import com.lucas.chessapi.security.jwt.JwtFilter;
import com.lucas.chessapi.security.jwt.JwtTokenValidator;
import com.lucas.chessapi.security.jwt.TokenProcessor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static com.lucas.chessapi.builders.JwtTokenDtoFactory.jwtTokenDtoWithSubject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContextJwtFilterTest extends TestContextHelper {
    @Mock
    UserRepository repository;
    @Mock
    TokenProcessor tokenProcessor;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private JwtTokenValidator validator;

    @InjectMocks
    private JwtFilter filter;

    private UserEntity user;
    private String token;

    protected void givenUserIsFound(
            UserEntity user,
            String forToken
    ) throws ServletException, IOException {
        this.user = user;
        this.token = forToken;
        when(request.getHeader("Authorization")).thenReturn("Bearer " + forToken);
        doNothing().when(validator).validate(forToken);
        when(tokenProcessor.getJwtTokenDtoFromToken(forToken))
                .thenReturn(jwtTokenDtoWithSubject(user.getId().toString()));
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(filterChain).doFilter(request, response);
    }

    protected void givenNoUserIsFound(String forToken) {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + forToken);
        doNothing().when(validator).validate(forToken);
        when(tokenProcessor.getJwtTokenDtoFromToken(forToken))
                .thenReturn(jwtTokenDtoWithSubject("1"));
        when(repository.findById(1L)).thenReturn(Optional.empty());
    }

    protected void givenValidatorThrows(Exception error, String forToken) {
        token = forToken;
        when(request.getHeader("Authorization")).thenReturn("Bearer " + forToken);
        doThrow(error).when(validator).validate(forToken);
    }

    protected void givenHeaderIsNullFor(String header) {
        when(request.getHeader(header)).thenReturn(null);
    }

    protected void givenHeaderWithoutPrefixFor(String header) {
        when(request.getHeader(header)).thenReturn("Something");
    }

    protected void whenFilterIsCalled() {
        try {
            filter.doFilter(request, response, filterChain);
        } catch (Exception e) {
            error = e;
        }
    }

    protected void thenSecurityContextShouldHaveUser() throws ServletException, IOException {
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .isInstanceOf(UserEntity.class)
                .usingRecursiveComparison()
                .isEqualTo(user);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(validator, times(1)).validate(token);
    }

    protected void thenShouldNeverProcessToken() throws ServletException, IOException {
        verify(tokenProcessor, never()).getJwtTokenDtoFromToken(anyString());
        verify(repository, never()).findById(anyLong());
    }

    protected void thenShouldCallValidator() {
        verify(validator, times(1)).validate(token);
    }

    protected void thenShouldNotCallValidator() {
        verify(validator, never()).validate(anyString());
    }

    protected void thenSecurityContextShouldHaveNothing() {
        assertThat(SecurityContextHolder.getContext().getAuthentication())
                .isNull();
    }
}
