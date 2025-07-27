package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.model.Endereco;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PedidoResponseTest {

    @Test
    void deveCriarPedidoResponseVazio() {
        // When
        PedidoResponse response = new PedidoResponse();
        
        // Then
        assertNull(response.getId());
        assertNull(response.getClienteId());
        assertNull(response.getRestauranteId());
        assertNull(response.getEnderecoEntrega());
        assertNull(response.getTotal());
        assertNull(response.getStatus());
        assertNull(response.getDataPedido());
        assertNull(response.getItens());
    }

    @Test
    void deveCriarPedidoResponseComParametros() {
        // Given
        Long id = 1L;
        Long clienteId = 10L;
        Long restauranteId = 5L;
        Endereco endereco = criarEnderecoMock();
        BigDecimal total = new BigDecimal("45.99");
        StatusPedido status = StatusPedido.CONFIRMADO;
        LocalDateTime dataPedido = LocalDateTime.now();
        List<ItemPedidoResponse> itens = criarItensMock();
        
        // When
        PedidoResponse response = new PedidoResponse(id, clienteId, restauranteId, endereco, 
                                                   total, status, dataPedido, itens);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(clienteId, response.getClienteId());
        assertEquals(restauranteId, response.getRestauranteId());
        assertEquals(endereco, response.getEnderecoEntrega());
        assertEquals(total, response.getTotal());
        assertEquals(status, response.getStatus());
        assertEquals(dataPedido, response.getDataPedido());
        assertEquals(itens, response.getItens());
    }

    @Test
    void deveDefinirEObterTodosOsCampos() {
        // Given
        PedidoResponse response = new PedidoResponse();
        Long id = 2L;
        Long clienteId = 20L;
        Long restauranteId = 15L;
        Endereco endereco = criarEnderecoMock();
        BigDecimal total = new BigDecimal("67.50");
        StatusPedido status = StatusPedido.EM_PREPARACAO;
        LocalDateTime dataPedido = LocalDateTime.now();
        List<ItemPedidoResponse> itens = criarItensMock();
        
        // When
        response.setId(id);
        response.setClienteId(clienteId);
        response.setRestauranteId(restauranteId);
        response.setEnderecoEntrega(endereco);
        response.setTotal(total);
        response.setStatus(status);
        response.setDataPedido(dataPedido);
        response.setItens(itens);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(clienteId, response.getClienteId());
        assertEquals(restauranteId, response.getRestauranteId());
        assertEquals(endereco, response.getEnderecoEntrega());
        assertEquals(total, response.getTotal());
        assertEquals(status, response.getStatus());
        assertEquals(dataPedido, response.getDataPedido());
        assertEquals(itens, response.getItens());
    }

    @Test
    void deveTrabalharComDiferentesStatusPedido() {
        // Given
        StatusPedido[] statusList = {
            StatusPedido.CRIADO,
            StatusPedido.CONFIRMADO,
            StatusPedido.EM_PREPARACAO,
            StatusPedido.ENVIADO,
            StatusPedido.ENTREGUE,
            StatusPedido.CANCELADO
        };
        
        for (StatusPedido status : statusList) {
            // When
            PedidoResponse response = new PedidoResponse();
            response.setStatus(status);
            
            // Then
            assertEquals(status, response.getStatus());
        }
    }

    @Test
    void deveTrabalharComDiferentesValoresTotais() {
        // Given
        BigDecimal[] totais = {
            new BigDecimal("10.50"),
            new BigDecimal("25.99"),
            new BigDecimal("100.00"),
            new BigDecimal("0.00")
        };
        
        for (BigDecimal total : totais) {
            // When
            PedidoResponse response = new PedidoResponse();
            response.setTotal(total);
            
            // Then
            assertEquals(total, response.getTotal());
        }
    }

    @Test
    void deveTrabalharComListaDeItensVazia() {
        // Given
        PedidoResponse response = new PedidoResponse();
        List<ItemPedidoResponse> itensVazios = new ArrayList<>();
        
        // When
        response.setItens(itensVazios);
        
        // Then
        assertEquals(itensVazios, response.getItens());
        assertTrue(response.getItens().isEmpty());
    }

    @Test
    void devePermitirValoresNulos() {
        // Given
        PedidoResponse response = new PedidoResponse();
        
        // When - definindo valores nulos
        response.setId(null);
        response.setClienteId(null);
        response.setRestauranteId(null);
        response.setEnderecoEntrega(null);
        response.setTotal(null);
        response.setStatus(null);
        response.setDataPedido(null);
        response.setItens(null);
        
        // Then
        assertNull(response.getId());
        assertNull(response.getClienteId());
        assertNull(response.getRestauranteId());
        assertNull(response.getEnderecoEntrega());
        assertNull(response.getTotal());
        assertNull(response.getStatus());
        assertNull(response.getDataPedido());
        assertNull(response.getItens());
    }

    // Métodos auxiliares para criar mocks
    private Endereco criarEnderecoMock() {
        Endereco endereco = new Endereco();
        endereco.setCep("01234-567");
        endereco.setRua("Rua das Flores");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        return endereco;
    }

    private List<ItemPedidoResponse> criarItensMock() {
        ItemPedidoResponse item1 = new ItemPedidoResponse();
        item1.setProdutoId(10L);
        item1.setNomeProduto("Pizza Margherita");
        item1.setQuantidade(2);
        item1.setPrecoUnitario(new BigDecimal("15.50"));

        ItemPedidoResponse item2 = new ItemPedidoResponse();
        item2.setProdutoId(11L);
        item2.setNomeProduto("Refrigerante");
        item2.setQuantidade(1);
        item2.setPrecoUnitario(new BigDecimal("5.99"));

        return Arrays.asList(item1, item2);
    }
}
