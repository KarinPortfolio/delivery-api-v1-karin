package com.deliverytech.api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.deliverytech.api.model.Restaurante;

@DataJpaTest
class RestauranteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private Restaurante restaurantePizza;
    private Restaurante restauranteJapones;
    private Restaurante restauranteInativo;
    private Restaurante restauranteBrasileiro;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // Criar e persistir restaurantes para os testes
        restaurantePizza = new Restaurante();
        restaurantePizza.setNome("Pizzaria Deliciosa");
        restaurantePizza.setTelefone("551112345678");
        restaurantePizza.setCategoria("Pizza");
        restaurantePizza.setTaxaEntrega(new BigDecimal("7.50"));
        restaurantePizza.setTempoEntregaMinutos(30);
        restaurantePizza.setAtivo(true);
        entityManager.persistAndFlush(restaurantePizza);

        restauranteJapones = new Restaurante();
        restauranteJapones.setNome("Sushi Master");
        restauranteJapones.setTelefone("551198765432");
        restauranteJapones.setCategoria("Japonesa");
        restauranteJapones.setTaxaEntrega(new BigDecimal("10.00"));
        restauranteJapones.setTempoEntregaMinutos(45);
        restauranteJapones.setAtivo(true);
        entityManager.persistAndFlush(restauranteJapones);

        restauranteInativo = new Restaurante();
        restauranteInativo.setNome("Restaurante Antigo");
        restauranteInativo.setTelefone("551155554444");
        restauranteInativo.setCategoria("Variada");
        restauranteInativo.setTaxaEntrega(new BigDecimal("8.00"));
        restauranteInativo.setTempoEntregaMinutos(60);
        restauranteInativo.setAtivo(false); // Este restaurante estará inativo
        entityManager.persistAndFlush(restauranteInativo);

        restauranteBrasileiro = new Restaurante();
        restauranteBrasileiro.setNome("Comida Caseira da Vovó");
        restauranteBrasileiro.setTelefone("551133332222");
        restauranteBrasileiro.setCategoria("Brasileira");
        restauranteBrasileiro.setTaxaEntrega(new BigDecimal("6.50"));
        restauranteBrasileiro.setTempoEntregaMinutos(25);
        restauranteBrasileiro.setAtivo(true);
        entityManager.persistAndFlush(restauranteBrasileiro);
    }

    // --- Testes para findByCategoria(String categoria) ---

    @Test
    @DisplayName("Deve encontrar restaurantes por categoria existente")
    void deveEncontrarRestaurantesPorCategoria() {
        // Act
        List<Restaurante> restaurantesDePizza = restauranteRepository.findByCategoria("Pizza");
        List<Restaurante> restaurantesDeJaponesa = restauranteRepository.findByCategoria("Japonesa");
        List<Restaurante> restaurantesDeBrasileira = restauranteRepository.findByCategoria("Brasileira");

        // Assert
        assertNotNull(restaurantesDePizza);
        assertEquals(1, restaurantesDePizza.size());
        assertEquals("Pizzaria Deliciosa", restaurantesDePizza.get(0).getNome());

        assertNotNull(restaurantesDeJaponesa);
        assertEquals(1, restaurantesDeJaponesa.size());
        assertEquals("Sushi Master", restaurantesDeJaponesa.get(0).getNome());

        assertNotNull(restaurantesDeBrasileira);
        assertEquals(1, restaurantesDeBrasileira.size());
        assertEquals("Comida Caseira da Vovó", restaurantesDeBrasileira.get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum restaurante for encontrado para a categoria")
    void deveRetornarListaVaziaParaCategoriaInexistente() {
        // Act
        List<Restaurante> restaurantesDeCategoriaInexistente = restauranteRepository.findByCategoria("Mexicana");

        // Assert
        assertNotNull(restaurantesDeCategoriaInexistente);
        assertTrue(restaurantesDeCategoriaInexistente.isEmpty());
    }

    // --- Testes para findByAtivoTrue() ---

    @Test
    @DisplayName("Deve encontrar apenas restaurantes ativos")
    void deveEncontrarApenasRestaurantesAtivos() {
        // Act
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();

        // Assert
        assertNotNull(restaurantesAtivos);
        assertEquals(3, restaurantesAtivos.size()); // Pizzaria Deliciosa, Sushi Master, Comida Caseira da Vovó
        assertTrue(restaurantesAtivos.stream().allMatch(Restaurante::getAtivo));
        assertTrue(restaurantesAtivos.stream().anyMatch(r -> r.getNome().equals("Pizzaria Deliciosa")));
        assertTrue(restaurantesAtivos.stream().anyMatch(r -> r.getNome().equals("Sushi Master")));
        assertTrue(restaurantesAtivos.stream().anyMatch(r -> r.getNome().equals("Comida Caseira da Vovó")));
        assertFalse(restaurantesAtivos.stream().anyMatch(r -> r.getNome().equals("Restaurante Antigo"))); // Inativo
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum restaurante estiver ativo")
    void deveRetornarListaVaziaSeNenhumRestauranteEstiverAtivo() {
        // Arrange
        // Desativar todos os restaurantes existentes
        restaurantePizza.setAtivo(false);
        restauranteJapones.setAtivo(false);
        restauranteBrasileiro.setAtivo(false);
        entityManager.merge(restaurantePizza);
        entityManager.merge(restauranteJapones);
        entityManager.merge(restauranteBrasileiro);
        entityManager.flush();

        // Act
        List<Restaurante> restaurantesAtivos = restauranteRepository.findByAtivoTrue();

        // Assert
        assertNotNull(restaurantesAtivos);
        assertTrue(restaurantesAtivos.isEmpty());
    }

    // --- Testes para findByNome(String nome) ---

    @Test
    @DisplayName("Deve encontrar restaurante por nome existente")
    void deveEncontrarRestaurantePorNomeExistente() {
        // Act
        Optional<Restaurante> foundRestaurante = restauranteRepository.findByNome("Pizzaria Deliciosa");

        // Assert
        assertTrue(foundRestaurante.isPresent());
        assertEquals("Pizzaria Deliciosa", foundRestaurante.get().getNome());
        assertEquals("Pizza", foundRestaurante.get().getCategoria());
    }

    @Test
    @DisplayName("Não deve encontrar restaurante por nome inexistente")
    void naoDeveEncontrarRestaurantePorNomeInexistente() {
        // Act
        Optional<Restaurante> foundRestaurante = restauranteRepository.findByNome("Restaurante Inexistente");

        // Assert
        assertFalse(foundRestaurante.isPresent());
    }

    // --- Testes para métodos herdados de JpaRepository (básicos) ---

    @Test
    @DisplayName("Deve salvar um restaurante corretamente")
    void deveSalvarRestauranteCorretamente() {
        // Arrange
        Restaurante novoRestaurante = new Restaurante();
        novoRestaurante.setNome("Novo Restaurante Teste");
        novoRestaurante.setTelefone("5511000000000");
        novoRestaurante.setCategoria("Fast Food");
        novoRestaurante.setTaxaEntrega(new BigDecimal("5.00"));
        novoRestaurante.setTempoEntregaMinutos(20);
        novoRestaurante.setAtivo(true);

        // Act
        Restaurante restauranteSalvo = restauranteRepository.save(novoRestaurante);

        // Assert
        assertNotNull(restauranteSalvo.getId());
        assertEquals("Novo Restaurante Teste", restauranteSalvo.getNome());
        assertEquals("Fast Food", restauranteSalvo.getCategoria());
        assertTrue(restauranteSalvo.getAtivo());

        // Verificar se foi salvo no banco de dados
        Restaurante restauranteEncontrado = entityManager.find(Restaurante.class, restauranteSalvo.getId());
        assertNotNull(restauranteEncontrado);
        assertEquals("Novo Restaurante Teste", restauranteEncontrado.getNome());
    }

    @Test
    @DisplayName("Deve encontrar restaurante por ID existente")
    void deveEncontrarRestaurantePorIdExistente() {
        // Act
        Optional<Restaurante> foundRestaurante = restauranteRepository.findById(restaurantePizza.getId());

        // Assert
        assertTrue(foundRestaurante.isPresent());
        assertEquals("Pizzaria Deliciosa", foundRestaurante.get().getNome());
    }

    @Test
    @DisplayName("Não deve encontrar restaurante por ID inexistente")
    void naoDeveEncontrarRestaurantePorIdInexistente() {
        // Act
        Optional<Restaurante> foundRestaurante = restauranteRepository.findById(999L); // ID que não existe

        // Assert
        assertFalse(foundRestaurante.isPresent());
    }

    @Test
    @DisplayName("Deve retornar todos os restaurantes")
    void deveRetornarTodosOsRestaurantes() {
        // Act
        List<Restaurante> allRestaurantes = restauranteRepository.findAll();

        // Assert
        assertNotNull(allRestaurantes);
        assertEquals(4, allRestaurantes.size()); // Todos os 4 restaurantes criados no setUp
        assertTrue(allRestaurantes.stream().anyMatch(r -> r.getNome().equals("Pizzaria Deliciosa")));
        assertTrue(allRestaurantes.stream().anyMatch(r -> r.getNome().equals("Sushi Master")));
        assertTrue(allRestaurantes.stream().anyMatch(r -> r.getNome().equals("Restaurante Antigo")));
        assertTrue(allRestaurantes.stream().anyMatch(r -> r.getNome().equals("Comida Caseira da Vovó")));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver restaurantes")
    void deveRetornarListaVaziaSeNaoHouverRestaurantes() {
        // Arrange
        entityManager.clear(); // Limpa os restaurantes persistidos no setUp
        restauranteRepository.deleteAll(); // Garante que não há restaurantes no banco antes do teste

        // Act
        List<Restaurante> allRestaurantes = restauranteRepository.findAll();

        // Assert
        assertNotNull(allRestaurantes);
        assertTrue(allRestaurantes.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um restaurante por ID existente")
    void deveDeletarRestaurantePorIdExistente() {
        // Arrange
        Long idToDelete = restaurantePizza.getId();
        assertTrue(restauranteRepository.findById(idToDelete).isPresent()); // Verifica que existe antes de deletar

        // Act
        restauranteRepository.deleteById(idToDelete);
        entityManager.flush(); // Garante que a operação de delete foi sincronizada

        // Assert
        assertFalse(restauranteRepository.findById(idToDelete).isPresent()); // Verifica que não existe mais
    }

    @Test
    @DisplayName("Não deve lançar exceção ao tentar deletar restaurante por ID inexistente")
    void naoDeveLancarExcecaoAoDeletarRestauranteInexistente() {
        // Act & Assert
        assertDoesNotThrow(() -> restauranteRepository.deleteById(999L)); // Tenta deletar um ID que não existe
        // O JpaRepository.deleteById não lança exceção se o ID não for encontrado.
    }
}
