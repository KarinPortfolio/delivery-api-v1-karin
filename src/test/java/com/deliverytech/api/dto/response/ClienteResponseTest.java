package com.deliverytech.api.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteResponseTest {

    @Test
    void deveTestarConstrutorVazio() {
        // Act
        ClienteResponse response = new ClienteResponse();

        // Assert
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getNome());
        assertNull(response.getEmail());
        assertNull(response.getAtivo());
    }

    @Test
    void deveTestarConstrutorCompleto() {
        // Arrange
        Long id = 1L;
        String nome = "João Silva";
        String email = "joao@example.com";
        Boolean ativo = true;

        // Act
        ClienteResponse response = new ClienteResponse(id, nome, email, ativo);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
        assertEquals(email, response.getEmail());
        assertEquals(ativo, response.getAtivo());
    }

    @Test
    void deveTestarSetters() {
        // Arrange
        ClienteResponse response = new ClienteResponse();
        Long id = 2L;
        String nome = "Maria Silva";
        String email = "maria@example.com";
        Boolean ativo = false;

        // Act
        response.setId(id);
        response.setNome(nome);
        response.setEmail(email);
        response.setAtivo(ativo);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
        assertEquals(email, response.getEmail());
        assertEquals(ativo, response.getAtivo());
    }

    @Test
    void deveTestarToString() {
        // Arrange
        ClienteResponse response = new ClienteResponse(1L, "Test", "test@test.com", true);

        // Act & Assert
        String toString = response.toString();
        assertNotNull(toString);
    }
}
