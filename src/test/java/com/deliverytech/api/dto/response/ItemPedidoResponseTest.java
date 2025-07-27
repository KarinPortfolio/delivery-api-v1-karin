package com.deliverytech.api.dto.response;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoResponseTest {

    @Test
    void deveCriarItemPedidoResponseVazio() {
        // When
        ItemPedidoResponse response = new ItemPedidoResponse();
        
        // Then
        assertNull(response.getProdutoId());
        assertNull(response.getNomeProduto());
        assertNull(response.getQuantidade());
        assertNull(response.getPrecoUnitario());
    }

    @Test
    void deveCriarItemPedidoResponseComParametros() {
        // Given
        Long produtoId = 1L;
        String nomeProduto = "Pizza Margherita";
        Integer quantidade = 2;
        BigDecimal precoUnitario = new BigDecimal("25.50");
        
        // When
        ItemPedidoResponse response = new ItemPedidoResponse(produtoId, nomeProduto, quantidade, precoUnitario);
        
        // Then
        assertEquals(produtoId, response.getProdutoId());
        assertEquals(nomeProduto, response.getNomeProduto());
        assertEquals(quantidade, response.getQuantidade());
        assertEquals(precoUnitario, response.getPrecoUnitario());
    }

    @Test
    void deveDefinirEObterTodosOsCampos() {
        // Given
        ItemPedidoResponse response = new ItemPedidoResponse();
        Long produtoId = 5L;
        String nomeProduto = "Hambúrguer Artesanal";
        Integer quantidade = 3;
        BigDecimal precoUnitario = new BigDecimal("18.99");
        
        // When
        response.setProdutoId(produtoId);
        response.setNomeProduto(nomeProduto);
        response.setQuantidade(quantidade);
        response.setPrecoUnitario(precoUnitario);
        
        // Then
        assertEquals(produtoId, response.getProdutoId());
        assertEquals(nomeProduto, response.getNomeProduto());
        assertEquals(quantidade, response.getQuantidade());
        assertEquals(precoUnitario, response.getPrecoUnitario());
    }

    @Test
    void deveTrabalharComDiferentesQuantidades() {
        // Given
        Integer[] quantidades = {1, 2, 5, 10, 100};
        
        for (Integer quantidade : quantidades) {
            // When
            ItemPedidoResponse response = new ItemPedidoResponse();
            response.setQuantidade(quantidade);
            
            // Then
            assertEquals(quantidade, response.getQuantidade());
        }
    }

    @Test
    void deveTrabalharComDiferentesPrecos() {
        // Given
        BigDecimal[] precos = {
            new BigDecimal("5.50"),
            new BigDecimal("10.00"),
            new BigDecimal("25.99"),
            new BigDecimal("100.50"),
            new BigDecimal("0.99")
        };
        
        for (BigDecimal preco : precos) {
            // When
            ItemPedidoResponse response = new ItemPedidoResponse();
            response.setPrecoUnitario(preco);
            
            // Then
            assertEquals(preco, response.getPrecoUnitario());
        }
    }

    @Test
    void deveTrabalharComDiferentesNomesProdutos() {
        // Given
        String[] nomes = {
            "Pizza Margherita",
            "Hambúrguer Bacon",
            "Refrigerante Cola",
            "Batata Frita",
            "Sorvete de Chocolate"
        };
        
        for (String nome : nomes) {
            // When
            ItemPedidoResponse response = new ItemPedidoResponse();
            response.setNomeProduto(nome);
            
            // Then
            assertEquals(nome, response.getNomeProduto());
        }
    }

    @Test
    void deveCalcularValorTotalImplicito() {
        // Given
        ItemPedidoResponse response = new ItemPedidoResponse();
        Integer quantidade = 3;
        BigDecimal precoUnitario = new BigDecimal("15.50");
        
        // When
        response.setQuantidade(quantidade);
        response.setPrecoUnitario(precoUnitario);
        
        // Then
        assertEquals(quantidade, response.getQuantidade());
        assertEquals(precoUnitario, response.getPrecoUnitario());
        
        // Verificação implícita do cálculo (3 * 15.50 = 46.50)
        BigDecimal totalEsperado = precoUnitario.multiply(new BigDecimal(quantidade));
        assertEquals(new BigDecimal("46.50"), totalEsperado);
    }

    @Test
    void devePermitirValoresNulos() {
        // Given
        ItemPedidoResponse response = new ItemPedidoResponse();
        
        // When - definindo valores nulos
        response.setProdutoId(null);
        response.setNomeProduto(null);
        response.setQuantidade(null);
        response.setPrecoUnitario(null);
        
        // Then
        assertNull(response.getProdutoId());
        assertNull(response.getNomeProduto());
        assertNull(response.getQuantidade());
        assertNull(response.getPrecoUnitario());
    }

    @Test
    void deveTrabalharComQuantidadeZero() {
        // Given
        ItemPedidoResponse response = new ItemPedidoResponse();
        Integer quantidadeZero = 0;
        
        // When
        response.setQuantidade(quantidadeZero);
        
        // Then
        assertEquals(quantidadeZero, response.getQuantidade());
    }

    @Test
    void deveTrabalharComPrecoZero() {
        // Given
        ItemPedidoResponse response = new ItemPedidoResponse();
        BigDecimal precoZero = new BigDecimal("0.00");
        
        // When
        response.setPrecoUnitario(precoZero);
        
        // Then
        assertEquals(precoZero, response.getPrecoUnitario());
    }
}
