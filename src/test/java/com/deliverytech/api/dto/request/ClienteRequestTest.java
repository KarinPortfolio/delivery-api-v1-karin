package com.deliverytech.api.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteRequestTest {

    @Test
    void deveTestarGettersESetters() {
        // Arrange
        ClienteRequest request = new ClienteRequest();
        String nome = "João Silva";
        String email = "joao@example.com";

        // Act
        request.setNome(nome);
        request.setEmail(email);

        // Assert
        assertEquals(nome, request.getNome());
        assertEquals(email, request.getEmail());
    }

    @Test
    void deveTestarConstrutor() {
        // Act
        ClienteRequest request = new ClienteRequest();

        // Assert
        assertNotNull(request);
        assertNull(request.getNome());
        assertNull(request.getEmail());
    }

    @Test
    void deveTestarToString() {
        // Arrange
        ClienteRequest request = new ClienteRequest();
        request.setNome("Test");
        request.setEmail("test@test.com");

        // Act & Assert
        String toString = request.toString();
        assertNotNull(toString);
    }
}
