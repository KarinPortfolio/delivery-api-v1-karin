package com.deliverytech.api.dto.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRequestTest {

    @Test
    void deveTestarGettersESetters() {
        // Arrange
        ProdutoRequest request = new ProdutoRequest();
        String nome = "Pizza Margherita";
        String descricao = "Pizza deliciosa";

        // Act
        request.setNome(nome);
        request.setDescricao(descricao);

        // Assert
        assertEquals(nome, request.getNome());
        assertEquals(descricao, request.getDescricao());
    }

    @Test
    void deveTestarConstrutor() {
        // Act
        ProdutoRequest request = new ProdutoRequest();

        // Assert
        assertNotNull(request);
    }
}
