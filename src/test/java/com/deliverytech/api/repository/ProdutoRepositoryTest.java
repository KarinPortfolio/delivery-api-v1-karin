package com.deliverytech.api.repository;

import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProdutoRepository produtoRepository;

    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private Produto pizzaMargherita;
    private Produto refrigerante;
    private Produto lasanha;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // Criar restaurantes
        restaurante1 = new Restaurante();
        restaurante1.setNome("Pizzaria Central");
        restaurante1.setTelefone("11987654321");
        restaurante1.setCategoria("Pizza");
        restaurante1.setTaxaEntrega(new BigDecimal("5.00"));
        restaurante1.setTempoEntregaMinutos(30);
        restaurante1.setAtivo(true);
        entityManager.persist(restaurante1);

        restaurante2 = new Restaurante();
        restaurante2.setNome("Restaurante Italiano");
        restaurante2.setTelefone("11912345678");
        restaurante2.setCategoria("Italiana");
        restaurante2.setTaxaEntrega(new BigDecimal("7.00"));
        restaurante2.setTempoEntregaMinutos(40);
        restaurante2.setAtivo(true);
        entityManager.persist(restaurante2);

        // Criar produtos e associá-los a restaurantes
        pizzaMargherita = new Produto();
        pizzaMargherita.setNome("Pizza Margherita");
        pizzaMargherita.setDescricao("Pizza tradicional de queijo e manjericão");
        pizzaMargherita.setPreco(new BigDecimal("25.90"));
        pizzaMargherita.setDisponivel(true);
        pizzaMargherita.setCategoria("Pizza");
        pizzaMargherita.setRestaurante(restaurante1);
        entityManager.persist(pizzaMargherita);

        refrigerante = new Produto();
        refrigerante.setNome("Refrigerante Cola");
        refrigerante.setDescricao("Lata de 350ml");
        refrigerante.setPreco(new BigDecimal("7.00"));
        refrigerante.setDisponivel(true);
        refrigerante.setCategoria("Bebidas");
        refrigerante.setRestaurante(restaurante1);
        entityManager.persist(refrigerante);

        lasanha = new Produto();
        lasanha.setNome("Lasanha Bolonhesa");
        lasanha.setDescricao("Lasanha com molho bolonhesa e queijo");
        lasanha.setPreco(new BigDecimal("35.00"));
        lasanha.setDisponivel(false); // Este produto estará indisponível
        lasanha.setCategoria("Italiana");
        lasanha.setRestaurante(restaurante2);
        entityManager.persist(lasanha);

        entityManager.flush(); // Sincroniza o contexto de persistência com o banco de dados
    }

    // --- Testes para findByRestauranteId(Long restauranteId) ---

    @Test
    @DisplayName("Deve encontrar produtos por ID de restaurante")
    void deveEncontrarProdutosPorRestauranteId() {
        // Act
        List<Produto> produtosDoRestaurante1 = produtoRepository.findByRestauranteId(restaurante1.getId());

        // Assert
        assertNotNull(produtosDoRestaurante1);
        assertEquals(2, produtosDoRestaurante1.size());
        assertTrue(produtosDoRestaurante1.stream().anyMatch(p -> p.getNome().equals("Pizza Margherita")));
        assertTrue(produtosDoRestaurante1.stream().anyMatch(p -> p.getNome().equals("Refrigerante Cola")));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum produto for encontrado para o ID do restaurante")
    void deveRetornarListaVaziaParaRestauranteIdInexistente() {
        // Act
        List<Produto> produtosDeRestauranteInexistente = produtoRepository.findByRestauranteId(999L); // ID que não existe

        // Assert
        assertNotNull(produtosDeRestauranteInexistente);
        assertTrue(produtosDeRestauranteInexistente.isEmpty());
    }

    // --- Testes para findByDisponivelTrue() ---

    @Test
    @DisplayName("Deve encontrar apenas produtos disponíveis")
    void deveEncontrarApenasProdutosDisponiveis() {
        // Act
        List<Produto> produtosDisponiveis = produtoRepository.findByDisponivelTrue();

        // Assert
        assertNotNull(produtosDisponiveis);
        assertEquals(2, produtosDisponiveis.size()); // Pizza Margherita e Refrigerante Cola
        assertTrue(produtosDisponiveis.stream().allMatch(Produto::getDisponivel));
        assertTrue(produtosDisponiveis.stream().anyMatch(p -> p.getNome().equals("Pizza Margherita")));
        assertTrue(produtosDisponiveis.stream().anyMatch(p -> p.getNome().equals("Refrigerante Cola")));
        assertFalse(produtosDisponiveis.stream().anyMatch(p -> p.getNome().equals("Lasanha Bolonhesa"))); // Lasanha está indisponível
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum produto estiver disponível")
    void deveRetornarListaVaziaSeNenhumProdutoEstiverDisponivel() {
        // Arrange
        // Desativar todos os produtos existentes
        pizzaMargherita.setDisponivel(false);
        refrigerante.setDisponivel(false);
        entityManager.merge(pizzaMargherita);
        entityManager.merge(refrigerante);
        entityManager.flush();

        // Act
        List<Produto> produtosDisponiveis = produtoRepository.findByDisponivelTrue();

        // Assert
        assertNotNull(produtosDisponiveis);
        assertTrue(produtosDisponiveis.isEmpty());
    }

    // --- Testes para findByCategoria(String categoria) ---

    @Test
    @DisplayName("Deve encontrar produtos por categoria")
    void deveEncontrarProdutosPorCategoria() {
        // Act
        List<Produto> produtosDePizza = produtoRepository.findByCategoria("Pizza");
        List<Produto> produtosDeBebidas = produtoRepository.findByCategoria("Bebidas");
        List<Produto> produtosDeItaliana = produtoRepository.findByCategoria("Italiana");

        // Assert
        assertNotNull(produtosDePizza);
        assertEquals(1, produtosDePizza.size());
        assertEquals("Pizza Margherita", produtosDePizza.get(0).getNome());

        assertNotNull(produtosDeBebidas);
        assertEquals(1, produtosDeBebidas.size());
        assertEquals("Refrigerante Cola", produtosDeBebidas.get(0).getNome());

        assertNotNull(produtosDeItaliana);
        assertEquals(1, produtosDeItaliana.size());
        assertEquals("Lasanha Bolonhesa", produtosDeItaliana.get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum produto for encontrado para a categoria")
    void deveRetornarListaVaziaParaCategoriaInexistente() {
        // Act
        List<Produto> produtosDeCategoriaInexistente = produtoRepository.findByCategoria("Sobremesas");

        // Assert
        assertNotNull(produtosDeCategoriaInexistente);
        assertTrue(produtosDeCategoriaInexistente.isEmpty());
    }
}