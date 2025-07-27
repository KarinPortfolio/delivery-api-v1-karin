package com.deliverytech.api.dto.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoResponseTest {

    @Test
    void deveTestarConstrutorVazio() {
        // Act
        ProdutoResponse response = new ProdutoResponse();

        // Assert
        assertNotNull(response);
    }

    @Test
    void deveTestarGettersESetters() {
        // Arrange
        ProdutoResponse response = new ProdutoResponse();
        Long id = 1L;
        String nome = "Pizza";

        // Act
        response.setId(id);
        response.setNome(nome);

        // Assert
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
    }
}
