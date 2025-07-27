package com.deliverytech.api.security;

import com.deliverytech.api.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private Usuario usuario;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        
        userDetails = User.builder()
                .username("test@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    void deveGerarToken() {
        // Act
        String token = jwtUtil.generateToken(userDetails, usuario);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void deveExtrairUsernameDoToken() {
        // Arrange
        String token = jwtUtil.generateToken(userDetails, usuario);

        // Act
        String username = jwtUtil.extractUsername(token);

        // Assert
        assertEquals("test@example.com", username);
    }

    @Test
    void deveValidarTokenValido() {
        // Arrange
        String token = jwtUtil.generateToken(userDetails, usuario);

        // Act
        boolean isValid = jwtUtil.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void deveExtrairClaims() {
        // Arrange
        String token = jwtUtil.generateToken(userDetails, usuario);

        // Act & Assert
        assertDoesNotThrow(() -> {
            String username = jwtUtil.extractUsername(token);
            assertEquals("test@example.com", username);
        });
    }

    @Test
    void deveTestarInstanciaUtil() {
        // Assert
        assertNotNull(jwtUtil);
        assertInstanceOf(JwtUtil.class, jwtUtil);
    }
}
