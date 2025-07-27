package com.deliverytech.api.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    @Test
    void deveTestarConstrutorVazio() {
        // Act
        LoginRequest request = new LoginRequest();

        // Assert
        assertNotNull(request);
        assertNull(request.getEmail());
        assertNull(request.getSenha());
    }

    @Test
    void deveTestarConstrutorCompleto() {
        // Arrange
        String email = "test@example.com";
        String senha = "password123";

        // Act
        LoginRequest request = new LoginRequest(email, senha);

        // Assert
        assertEquals(email, request.getEmail());
        assertEquals(senha, request.getSenha());
    }

    @Test
    void deveTestarSetters() {
        // Arrange
        LoginRequest request = new LoginRequest();
        String email = "new@example.com";
        String senha = "newPassword";

        // Act
        request.setEmail(email);
        request.setSenha(senha);

        // Assert
        assertEquals(email, request.getEmail());
        assertEquals(senha, request.getSenha());
    }

    @Test
    void deveTestarToString() {
        // Arrange
        LoginRequest request = new LoginRequest("test@test.com", "password");

        // Act & Assert
        String toString = request.toString();
        assertNotNull(toString);
    }
}
