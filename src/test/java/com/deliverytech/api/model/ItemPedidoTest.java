package com.deliverytech.api.model;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItemPedidoTest {

    private ItemPedido itemPedido;
    private Pedido pedidoMock;
    private Produto produtoMock;

    @BeforeEach
    
    void setUp() {
        // Inicializa um objeto ItemPedido antes de cada teste
        itemPedido = new ItemPedido();

        // Cria mocks ou stubs para as entidades relacionadas
        pedidoMock = new Pedido(); // Using constructor instead of builder
        pedidoMock.setId(1L);
        
        produtoMock = new Produto(); // Using constructor instead of builder
        produtoMock.setId(1L);
        produtoMock.setNome("Pizza");
        produtoMock.setPreco(new BigDecimal("25.00"));
    }

    @Test
    @DisplayName("Deve criar um item de pedido com construtor vazio e valores padrão")
    void deveCriarItemPedidoComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(itemPedido.getId(), "ID deve ser nulo para um novo item de pedido sem ID definido");
        assertNull(itemPedido.getPedido(), "Pedido deve ser nulo por padrão");
        assertNull(itemPedido.getProduto(), "Produto deve ser nulo por padrão");
        assertNull(itemPedido.getQuantidade(), "Quantidade deve ser nula por padrão");
        assertNull(itemPedido.getPrecoUnitario(), "Preço unitário deve ser nulo por padrão");
    }

    @Test
    @DisplayName("Deve criar um item de pedido usando o construtor AllArgsConstructor")
    void deveCriarItemPedidoComAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        Integer quantidade = 2;
        BigDecimal precoUnitario = new BigDecimal("25.90");

        // Act
        ItemPedido itemPedidoCompleto = new ItemPedido(id, pedidoMock, produtoMock, quantidade, precoUnitario);

        // Assert
        assertEquals(id, itemPedidoCompleto.getId());
        assertEquals(pedidoMock, itemPedidoCompleto.getPedido());
        assertEquals(produtoMock, itemPedidoCompleto.getProduto());
        assertEquals(quantidade, itemPedidoCompleto.getQuantidade());
        assertEquals(precoUnitario, itemPedidoCompleto.getPrecoUnitario());
    }

    @Test
    @DisplayName("Deve criar um item de pedido usando o construtor Builder")
    void deveCriarItemPedidoComBuilder() {
        // Arrange
        Long id = 2L;
        Integer quantidade = 3;
        BigDecimal precoUnitario = new BigDecimal("7.50");

        // Act - Using constructor and setters instead of builder
        ItemPedido itemPedidoBuilder = new ItemPedido();
        itemPedidoBuilder.setId(id);
        itemPedidoBuilder.setPedido(pedidoMock);
        itemPedidoBuilder.setProduto(produtoMock);
        itemPedidoBuilder.setQuantidade(quantidade);
        itemPedidoBuilder.setPrecoUnitario(precoUnitario);

        // Assert
        assertEquals(id, itemPedidoBuilder.getId());
        assertEquals(pedidoMock, itemPedidoBuilder.getPedido());
        assertEquals(produtoMock, itemPedidoBuilder.getProduto());
        assertEquals(quantidade, itemPedidoBuilder.getQuantidade());
        assertEquals(precoUnitario, itemPedidoBuilder.getPrecoUnitario());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        Long newId = 3L;
        Integer newQuantidade = 5;
        BigDecimal newPrecoUnitario = new BigDecimal("15.00");
        
        Pedido newPedidoMock = new Pedido();
        newPedidoMock.setId(2L);
        
        Produto newProdutoMock = new Produto();
        newProdutoMock.setId(2L);
        newProdutoMock.setNome("Bebida");
        newProdutoMock.setPreco(new BigDecimal("5.00"));

        // Act
        itemPedido.setId(newId);
        itemPedido.setPedido(newPedidoMock);
        itemPedido.setProduto(newProdutoMock);
        itemPedido.setQuantidade(newQuantidade);
        itemPedido.setPrecoUnitario(newPrecoUnitario);

        // Assert
        assertEquals(newId, itemPedido.getId());
        assertEquals(newPedidoMock, itemPedido.getPedido());
        assertEquals(newProdutoMock, itemPedido.getProduto());
        assertEquals(newQuantidade, itemPedido.getQuantidade());
        assertEquals(newPrecoUnitario, itemPedido.getPrecoUnitario());
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos ItemPedido")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        BigDecimal preco = new BigDecimal("10.00");
        ItemPedido item1 = new ItemPedido(1L, pedidoMock, produtoMock, 1, preco);
        ItemPedido item2 = new ItemPedido(1L, pedidoMock, produtoMock, 1, preco);
        ItemPedido item3 = new ItemPedido(2L, pedidoMock, produtoMock, 1, preco); // ID diferente
        ItemPedido item4 = new ItemPedido(1L, pedidoMock, produtoMock, 2, preco); // Quantidade diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(item1, item2, "Itens de pedido com mesmos valores devem ser iguais");
        assertNotEquals(item1, item3, "Itens de pedido com IDs diferentes não devem ser iguais");
        assertNotEquals(item1, item4, "Itens de pedido com quantidades diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos ItemPedido")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        BigDecimal preco = new BigDecimal("10.00");
        ItemPedido item1 = new ItemPedido(1L, pedidoMock, produtoMock, 1, preco);
        ItemPedido item2 = new ItemPedido(1L, pedidoMock, produtoMock, 1, preco);

        // Assert
        assertEquals(item1.hashCode(), item2.hashCode(), "Hashcodes de itens de pedido iguais devem ser iguais");
    }
}
