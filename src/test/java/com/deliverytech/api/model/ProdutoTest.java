package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    private Produto produto;
    private Restaurante restauranteMock;

    @BeforeEach
    void setUp() {
        // Inicializa um objeto Produto antes de cada teste
        produto = new Produto();

        // Cria um mock ou stub para a entidade Restaurante
        restauranteMock = new Restaurante();
        restauranteMock.setId(1L);
        restauranteMock.setNome("Restaurante Teste");
    }

    @Test
    @DisplayName("Deve criar um produto com construtor vazio e valores padrão")
    void deveCriarProdutoComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(produto.getId(), "ID deve ser nulo para um novo produto sem ID definido");
        assertNull(produto.getNome(), "Nome deve ser nulo por padrão");
        assertNull(produto.getCategoria(), "Categoria deve ser nula por padrão");
        assertNull(produto.getDescricao(), "Descrição deve ser nula por padrão");
        assertNull(produto.getPreco(), "Preço deve ser nulo por padrão");
        assertTrue(produto.getDisponivel(), "Disponível deve ser true por padrão (@Builder.Default)");
        assertNull(produto.getRestaurante(), "Restaurante deve ser nulo por padrão");
    }

    @Test
    @DisplayName("Deve criar um produto usando o construtor AllArgsConstructor")
    void deveCriarProdutoComAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String nome = "Pizza Margherita";
        String categoria = "Pizzas";
        String descricao = "Pizza tradicional de queijo e manjericão";
        BigDecimal preco = new BigDecimal("25.90");
        Boolean disponivel = true;

        // Act
        Produto produtoCompleto = new Produto(id, nome, categoria, descricao, preco, disponivel, restauranteMock);

        // Assert
        assertEquals(id, produtoCompleto.getId());
        assertEquals(nome, produtoCompleto.getNome());
        assertEquals(categoria, produtoCompleto.getCategoria());
        assertEquals(descricao, produtoCompleto.getDescricao());
        assertEquals(preco, produtoCompleto.getPreco());
        assertEquals(disponivel, produtoCompleto.getDisponivel());
        assertEquals(restauranteMock, produtoCompleto.getRestaurante());
    }

    @Test
    @DisplayName("Deve criar um produto usando o construtor Builder")
    void deveCriarProdutoComBuilder() {
        // Arrange
        Long id = 2L;
        String nome = "Refrigerante Cola";
        String categoria = "Bebidas";
        String descricao = "Lata de 350ml";
        BigDecimal preco = new BigDecimal("7.00");

        // Act
        Produto produtoBuilder = new Produto();
        produtoBuilder.setId(id);
        produtoBuilder.setNome(nome);
        produtoBuilder.setCategoria(categoria);
        produtoBuilder.setDescricao(descricao);
        produtoBuilder.setPreco(preco);
        produtoBuilder.setDisponivel(false); // Explicitamente definido
        produtoBuilder.setRestaurante(restauranteMock);

        // Assert
        assertEquals(id, produtoBuilder.getId());
        assertEquals(nome, produtoBuilder.getNome());
        assertEquals(categoria, produtoBuilder.getCategoria());
        assertEquals(descricao, produtoBuilder.getDescricao());
        assertEquals(preco, produtoBuilder.getPreco());
        assertFalse(produtoBuilder.getDisponivel());
        assertEquals(restauranteMock, produtoBuilder.getRestaurante());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        Long newId = 3L;
        String newName = "Hambúrguer Clássico";
        String newCategory = "Lanches";
        String newDescription = "Pão, carne, queijo e salada";
        BigDecimal newPrice = new BigDecimal("18.50");
        Boolean newDisponivel = false;
        Restaurante newRestauranteMock = new Restaurante();
        newRestauranteMock.setId(2L);
        newRestauranteMock.setNome("Outro Restaurante");

        // Act
        produto.setId(newId);
        produto.setNome(newName);
        produto.setCategoria(newCategory);
        produto.setDescricao(newDescription);
        produto.setPreco(newPrice);
        produto.setDisponivel(newDisponivel);
        produto.setRestaurante(newRestauranteMock);

        // Assert
        assertEquals(newId, produto.getId());
        assertEquals(newName, produto.getNome());
        assertEquals(newCategory, produto.getCategoria());
        assertEquals(newDescription, produto.getDescricao());
        assertEquals(newPrice, produto.getPreco());
        assertEquals(newDisponivel, produto.getDisponivel());
        assertEquals(newRestauranteMock, produto.getRestaurante());
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'disponivel' quando não definido no builder")
    void deveVerificarDisponivelPadraoNoBuilder() {
        // Act
        Produto produtoSemDisponivel = new Produto();
        produtoSemDisponivel.setNome("Sobremesa");
        produtoSemDisponivel.setPreco(new BigDecimal("12.00"));

        // Assert
        assertTrue(produtoSemDisponivel.getDisponivel(), "Disponível deve ser true por padrão quando não especificado no builder");
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Produto")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        BigDecimal preco = new BigDecimal("20.00");
        Produto produto1 = new Produto(1L, "Item", "Cat", "Desc", preco, true, restauranteMock);
        Produto produto2 = new Produto(1L, "Item", "Cat", "Desc", preco, true, restauranteMock);
        Produto produto3 = new Produto(2L, "Item", "Cat", "Desc", preco, true, restauranteMock); // ID diferente
        Produto produto4 = new Produto(1L, "Outro Item", "Cat", "Desc", preco, true, restauranteMock); // Nome diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(produto1, produto2, "Produtos com mesmos valores devem ser iguais");
        assertNotEquals(produto1, produto3, "Produtos com IDs diferentes não devem ser iguais");
        assertNotEquals(produto1, produto4, "Produtos com nomes diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Produto")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        BigDecimal preco = new BigDecimal("20.00");
        Produto produto1 = new Produto(1L, "Item", "Cat", "Desc", preco, true, restauranteMock);
        Produto produto2 = new Produto(1L, "Item", "Cat", "Desc", preco, true, restauranteMock);

        // Assert
        assertEquals(produto1.hashCode(), produto2.hashCode(), "Hashcodes de produtos iguais devem ser iguais");
    }
}
