package com.deliverytech.api.dto.response;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class RestauranteResponseTest {

    @Test
    void deveCriarRestauranteResponseVazio() {
        // When
        RestauranteResponse response = new RestauranteResponse();
        
        // Then
        assertNull(response.getId());
        assertNull(response.getNome());
        assertNull(response.getCategoria());
        assertNull(response.getTelefone());
        assertNull(response.getTaxaEntrega());
        assertNull(response.getTempoEntregaMinutos());
        assertNull(response.getAtivo());
    }

    @Test
    void deveCriarRestauranteResponseComParametros() {
        // Given
        Long id = 1L;
        String nome = "Pizzaria Bella";
        String categoria = "Italiana";
        String telefone = "(11) 99999-9999";
        BigDecimal taxaEntrega = new BigDecimal("5.50");
        Integer tempoEntregaMinutos = 30;
        Boolean ativo = true;
        
        // When
        RestauranteResponse response = new RestauranteResponse(id, nome, categoria, telefone, taxaEntrega, tempoEntregaMinutos, ativo);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
        assertEquals(categoria, response.getCategoria());
        assertEquals(telefone, response.getTelefone());
        assertEquals(taxaEntrega, response.getTaxaEntrega());
        assertEquals(tempoEntregaMinutos, response.getTempoEntregaMinutos());
        assertEquals(ativo, response.getAtivo());
    }

    @Test
    void deveDefinirEObterTodosOsCampos() {
        // Given
        RestauranteResponse response = new RestauranteResponse();
        Long id = 3L;
        String nome = "Sushi House";
        String categoria = "Japonesa";
        String telefone = "(11) 77777-7777";
        BigDecimal taxaEntrega = new BigDecimal("7.50");
        Integer tempoEntregaMinutos = 45;
        Boolean ativo = false;
        
        // When
        response.setId(id);
        response.setNome(nome);
        response.setCategoria(categoria);
        response.setTelefone(telefone);
        response.setTaxaEntrega(taxaEntrega);
        response.setTempoEntregaMinutos(tempoEntregaMinutos);
        response.setAtivo(ativo);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
        assertEquals(categoria, response.getCategoria());
        assertEquals(telefone, response.getTelefone());
        assertEquals(taxaEntrega, response.getTaxaEntrega());
        assertEquals(tempoEntregaMinutos, response.getTempoEntregaMinutos());
        assertEquals(ativo, response.getAtivo());
    }

    @Test
    void deveTrabalharComDiferentesCategorias() {
        // Given
        String[] categorias = {"Italiana", "Japonesa", "Fast Food", "Brasileira", "Árabe"};
        
        for (String categoria : categorias) {
            // When
            RestauranteResponse response = new RestauranteResponse();
            response.setCategoria(categoria);
            
            // Then
            assertEquals(categoria, response.getCategoria());
        }
    }

    @Test
    void deveTrabalharComDiferentesTaxasDeEntrega() {
        // Given
        BigDecimal[] taxas = {
            new BigDecimal("0.00"),
            new BigDecimal("2.50"),
            new BigDecimal("5.99"),
            new BigDecimal("10.00")
        };
        
        for (BigDecimal taxa : taxas) {
            // When
            RestauranteResponse response = new RestauranteResponse();
            response.setTaxaEntrega(taxa);
            
            // Then
            assertEquals(taxa, response.getTaxaEntrega());
        }
    }

    @Test
    void deveTrabalharComDiferentesTemposDeEntrega() {
        // Given
        Integer[] tempos = {15, 30, 45, 60, 90};
        
        for (Integer tempo : tempos) {
            // When
            RestauranteResponse response = new RestauranteResponse();
            response.setTempoEntregaMinutos(tempo);
            
            // Then
            assertEquals(tempo, response.getTempoEntregaMinutos());
        }
    }

    @Test
    void deveManterConsistenciaComStatusAtivo() {
        // Given
        RestauranteResponse responseAtivo = new RestauranteResponse();
        RestauranteResponse responseInativo = new RestauranteResponse();
        
        // When
        responseAtivo.setAtivo(true);
        responseInativo.setAtivo(false);
        
        // Then
        assertTrue(responseAtivo.getAtivo());
        assertFalse(responseInativo.getAtivo());
    }

    @Test
    void devePermitirValoresNulos() {
        // Given
        RestauranteResponse response = new RestauranteResponse();
        
        // When - definindo valores nulos
        response.setId(null);
        response.setNome(null);
        response.setCategoria(null);
        response.setTelefone(null);
        response.setTaxaEntrega(null);
        response.setTempoEntregaMinutos(null);
        response.setAtivo(null);
        
        // Then
        assertNull(response.getId());
        assertNull(response.getNome());
        assertNull(response.getCategoria());
        assertNull(response.getTelefone());
        assertNull(response.getTaxaEntrega());
        assertNull(response.getTempoEntregaMinutos());
        assertNull(response.getAtivo());
    }
}
