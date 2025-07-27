package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Pedido pedido;
    private Cliente clienteMock;
    private Restaurante restauranteMock;
    private ItemPedido itemPedidoMock1;
    private ItemPedido itemPedidoMock2;
    private Endereco enderecoMock;

    @BeforeEach
    void setUp() {
        // Inicializa um objeto Pedido antes de cada teste
        pedido = new Pedido();

        // Cria mocks ou stubs para as entidades relacionadas
        clienteMock = new Cliente();
        clienteMock.setId(1L);
        clienteMock.setNome("Cliente Mock");
        
        restauranteMock = new Restaurante();
        restauranteMock.setId(1L);
        restauranteMock.setNome("Restaurante Mock");
        
        // Para ItemPedido, precisamos de um Produto mock
        Produto produtoMock1 = new Produto();
        produtoMock1.setId(1L);
        produtoMock1.setNome("Produto A");
        produtoMock1.setPreco(new BigDecimal("10.00"));
        
        Produto produtoMock2 = new Produto();
        produtoMock2.setId(2L);
        produtoMock2.setNome("Produto B");
        produtoMock2.setPreco(new BigDecimal("5.00"));

        itemPedidoMock1 = new ItemPedido();
        itemPedidoMock1.setId(1L);
        itemPedidoMock1.setProduto(produtoMock1);
        itemPedidoMock1.setQuantidade(2);
        itemPedidoMock1.setPrecoUnitario(new BigDecimal("10.00"));
        
        itemPedidoMock2 = new ItemPedido();
        itemPedidoMock2.setId(2L);
        itemPedidoMock2.setProduto(produtoMock2);
        itemPedidoMock2.setQuantidade(3);
        itemPedidoMock2.setPrecoUnitario(new BigDecimal("5.00"));

        enderecoMock = new Endereco();
        enderecoMock.setRua("Rua de Teste");
        enderecoMock.setNumero("123");
        enderecoMock.setBairro("Bairro Teste");
        enderecoMock.setCidade("Cidade Teste");
        enderecoMock.setEstado("TS");
        enderecoMock.setCep("00000-000");
    }

    @Test
    @DisplayName("Deve criar um pedido com construtor vazio e valores padrão")
    void deveCriarPedidoComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(pedido.getId(), "ID deve ser nulo para um novo pedido sem ID definido");
        assertNull(pedido.getCliente(), "Cliente deve ser nulo por padrão");
        assertNull(pedido.getRestaurante(), "Restaurante deve ser nulo por padrão");
        assertNull(pedido.getTotal(), "Total deve ser nulo por padrão");
        assertNull(pedido.getStatus(), "Status deve ser nulo por padrão");
        assertNotNull(pedido.getDataPedido(), "Data do pedido não deve ser nula por padrão (@Builder.Default)");
        assertTrue(pedido.getDataPedido().isBefore(LocalDateTime.now().plusSeconds(1)), "Data do pedido deve ser aproximadamente agora");
        assertNull(pedido.getItens(), "Lista de itens deve ser nula por padrão");
        assertNull(pedido.getEnderecoEntrega(), "Endereço de entrega deve ser nulo por padrão");
    }

    @Test
    @DisplayName("Deve criar um pedido usando o construtor AllArgsConstructor")
    void deveCriarPedidoComAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        BigDecimal total = new BigDecimal("89.90");
        StatusPedido status = StatusPedido.CRIADO;
        LocalDateTime dataPedido = LocalDateTime.of(2024, 1, 1, 12, 0);
        List<ItemPedido> itens = Arrays.asList(itemPedidoMock1, itemPedidoMock2);

        // Act
        Pedido pedidoCompleto = new Pedido(id, clienteMock, restauranteMock, total, status, dataPedido, itens, enderecoMock);

        // Assert
        assertEquals(id, pedidoCompleto.getId());
        assertEquals(clienteMock, pedidoCompleto.getCliente());
        assertEquals(restauranteMock, pedidoCompleto.getRestaurante());
        assertEquals(total, pedidoCompleto.getTotal());
        assertEquals(status, pedidoCompleto.getStatus());
        assertEquals(dataPedido, pedidoCompleto.getDataPedido());
        assertEquals(itens, pedidoCompleto.getItens());
        assertEquals(enderecoMock, pedidoCompleto.getEnderecoEntrega());
    }

    @Test
    @DisplayName("Deve criar um pedido usando setters")
    void deveCriarPedidoComSetters() {
        // Arrange
        Long id = 2L;
        BigDecimal total = new BigDecimal("50.00");
        StatusPedido status = StatusPedido.EM_PREPARACAO;
        LocalDateTime dataPedido = LocalDateTime.of(2023, 5, 10, 9, 30);

        // Act
        Pedido pedidoCompleto = new Pedido();
        pedidoCompleto.setId(id);
        pedidoCompleto.setCliente(clienteMock);
        pedidoCompleto.setRestaurante(restauranteMock);
        pedidoCompleto.setTotal(total);
        pedidoCompleto.setStatus(status);
        pedidoCompleto.setDataPedido(dataPedido);
        pedidoCompleto.setItens(new ArrayList<>());
        pedidoCompleto.setEnderecoEntrega(enderecoMock);

        // Assert
        assertEquals(id, pedidoCompleto.getId());
        assertEquals(clienteMock, pedidoCompleto.getCliente());
        assertEquals(restauranteMock, pedidoCompleto.getRestaurante());
        assertEquals(total, pedidoCompleto.getTotal());
        assertEquals(status, pedidoCompleto.getStatus());
        assertEquals(dataPedido, pedidoCompleto.getDataPedido());
        assertNotNull(pedidoCompleto.getItens());
        assertTrue(pedidoCompleto.getItens().isEmpty());
        assertEquals(enderecoMock, pedidoCompleto.getEnderecoEntrega());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        Long newId = 3L;
        Cliente newClienteMock = new Cliente();
        newClienteMock.setId(2L);
        
        Restaurante newRestauranteMock = new Restaurante();
        newRestauranteMock.setId(2L);
        
        BigDecimal newTotal = new BigDecimal("120.50");
        StatusPedido newStatus = StatusPedido.ENTREGUE;
        LocalDateTime newDataPedido = LocalDateTime.of(2022, 11, 20, 15, 45);
        List<ItemPedido> newItens = Arrays.asList(itemPedidoMock1);
        
        Endereco newEnderecoMock = new Endereco();
        newEnderecoMock.setRua("Nova Rua");

        // Act
        pedido.setId(newId);
        pedido.setCliente(newClienteMock);
        pedido.setRestaurante(newRestauranteMock);
        pedido.setTotal(newTotal);
        pedido.setStatus(newStatus);
        pedido.setDataPedido(newDataPedido);
        pedido.setItens(newItens);
        pedido.setEnderecoEntrega(newEnderecoMock);

        // Assert
        assertEquals(newId, pedido.getId());
        assertEquals(newClienteMock, pedido.getCliente());
        assertEquals(newRestauranteMock, pedido.getRestaurante());
        assertEquals(newTotal, pedido.getTotal());
        assertEquals(newStatus, pedido.getStatus());
        assertEquals(newDataPedido, pedido.getDataPedido());
        assertEquals(newItens, pedido.getItens());
        assertEquals(newEnderecoMock, pedido.getEnderecoEntrega());
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'dataPedido' quando não definido")
    void deveVerificarDataPedidoPadrao() {
        // Act
        Pedido pedidoSemDataPedido = new Pedido();
        pedidoSemDataPedido.setCliente(clienteMock);
        pedidoSemDataPedido.setRestaurante(restauranteMock);
        pedidoSemDataPedido.setTotal(BigDecimal.ZERO);
        pedidoSemDataPedido.setStatus(StatusPedido.CRIADO);

        // Assert
        assertNotNull(pedidoSemDataPedido.getDataPedido(), "Data do pedido não deve ser nula por padrão");
        assertTrue(pedidoSemDataPedido.getDataPedido().isBefore(LocalDateTime.now().plusSeconds(1)), "Data do pedido deve ser aproximadamente agora");
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Pedido")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = new BigDecimal("50.00");
        List<ItemPedido> itens = new ArrayList<>();

        Pedido pedido1 = new Pedido(1L, clienteMock, restauranteMock, total, StatusPedido.CRIADO, now, itens, enderecoMock);
        Pedido pedido2 = new Pedido(1L, clienteMock, restauranteMock, total, StatusPedido.CRIADO, now, itens, enderecoMock);
        Pedido pedido3 = new Pedido(2L, clienteMock, restauranteMock, total, StatusPedido.CRIADO, now, itens, enderecoMock); // ID diferente
        Pedido pedido4 = new Pedido(1L, clienteMock, restauranteMock, total, StatusPedido.ENTREGUE, now, itens, enderecoMock); // Status diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(pedido1, pedido2, "Pedidos com mesmos valores devem ser iguais");
        assertNotEquals(pedido1, pedido3, "Pedidos com IDs diferentes não devem ser iguais");
        assertNotEquals(pedido1, pedido4, "Pedidos com status diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Pedido")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        BigDecimal total = new BigDecimal("50.00");
        List<ItemPedido> itens = new ArrayList<>();

        Pedido pedido1 = new Pedido(1L, clienteMock, restauranteMock, total, StatusPedido.CRIADO, now, itens, enderecoMock);
        Pedido pedido2 = new Pedido(1L, clienteMock, restauranteMock, total, StatusPedido.CRIADO, now, itens, enderecoMock);

        // Assert
        assertEquals(pedido1.hashCode(), pedido2.hashCode(), "Hashcodes de pedidos iguais devem ser iguais");
    }
}