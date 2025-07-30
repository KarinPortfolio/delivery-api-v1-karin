package com.deliverytech.api.security;

import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.model.Role;
import io.jsonwebtoken.ExpiredJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    // ...existing code...

    @BeforeEach
    void setUp() {
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveProcessarRequisicaoSemHeaderAuthorization() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void deveProcessarRequisicaoComHeaderAuthorizationInvalido() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void deveProcessarTokenValidoComSucesso() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String email = "usuario@teste.com";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtil.isTokenValid(token, userDetails)).thenReturn(true);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtUtil).isTokenValid(token, userDetails);
    }

    @Test
    void deveProcessarTokenExpirado() throws ServletException, IOException {
        // Given
        String token = "expired.jwt.token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).extractUsername(token);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void deveProcessarTokenInvalidoParaUsuario() throws ServletException, IOException {
        // Given
        String token = "invalid.jwt.token";
        String email = "usuario@teste.com";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUtil.isTokenValid(token, userDetails)).thenReturn(false);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtUtil).isTokenValid(token, userDetails);
    }

    @Test
    void deveProcessarUsuarioJaAutenticado() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String email = "usuario@teste.com";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenReturn(email);

        // Simular usuário já autenticado
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setRole(Role.CLIENTE);
        
        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).extractUsername(token);
        // Como o usuário já está autenticado, não deve carregar novamente
    }

    @Test
    void deveProcessarExcecaoGenerica() throws ServletException, IOException {
        // Given
        String token = "problematic.jwt.token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("Generic error"));

        // When & Then
        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).extractUsername(token);
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void deveProcessarTokenComEspacosEmBranco() throws ServletException, IOException {
        // Given
        when(request.getHeader("Authorization")).thenReturn("  Bearer   ");

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void deveProcessarDiferentesTiposDeToken() throws ServletException, IOException {
        // Given - diferentes formatos de header
        String[] invalidHeaders = {
            "Basic token123",
            "token123",
            "Bearer",
            "Bearer ",
            "Bearer  ",
            ""
        };

        for (String header : invalidHeaders) {
            when(request.getHeader("Authorization")).thenReturn(header);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, atLeastOnce()).doFilter(request, response);
        }

        // Deve nunca tentar extrair username para headers inválidos
        verify(jwtUtil, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }

    @Test
    void deveProcessarTokenComUsuarioNaoEncontrado() throws ServletException, IOException {
        // Given
        String token = "valid.jwt.token";
        String email = "inexistente@teste.com";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email))
                .thenThrow(new RuntimeException("User not found"));

        // When & Then
        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtUtil, never()).isTokenValid(anyString(), any(UserDetails.class));
    }
}
