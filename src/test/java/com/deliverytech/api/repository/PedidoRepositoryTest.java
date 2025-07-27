package com.deliverytech.api.repository;

import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.model.Endereco;
import com.deliverytech.api.model.ItemPedido;
import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.StatusPedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PedidoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PedidoRepository pedidoRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private Restaurante restaurante1;
    private Restaurante restaurante2;
    private Produto produto1;
    private Produto produto2;
    private Pedido pedido1; // CRIADO
    private Pedido pedido2; // EM_PREPARACAO
    private Pedido pedido3; // ENTREGUE (para teste de data)

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // 1. Criar e persistir Clientes
        cliente1 = new Cliente();
        cliente1.setNome("Cliente A");
        cliente1.setEmail("clienteA@test.com");
        cliente1.setAtivo(true);
        entityManager.persist(cliente1);

        cliente2 = new Cliente();
        cliente2.setNome("Cliente B");
        cliente2.setEmail("clienteB@test.com");
        cliente2.setAtivo(true);
        entityManager.persist(cliente2);

        // 2. Criar e persistir Restaurantes
        restaurante1 = new Restaurante();
        restaurante1.setNome("Restaurante X");
        restaurante1.setTelefone("1111");
        restaurante1.setCategoria("Comida");
        restaurante1.setTaxaEntrega(BigDecimal.valueOf(5.0));
        restaurante1.setTempoEntregaMinutos(30);
        restaurante1.setAtivo(true);
        entityManager.persist(restaurante1);

        restaurante2 = new Restaurante();
        restaurante2.setNome("Restaurante Y");
        restaurante2.setTelefone("2222");
        restaurante2.setCategoria("Bebida");
        restaurante2.setTaxaEntrega(BigDecimal.valueOf(3.0));
        restaurante2.setTempoEntregaMinutos(20);
        restaurante2.setAtivo(true);
        entityManager.persist(restaurante2);

        // 3. Criar e persistir Produtos
        produto1 = new Produto();
        produto1.setNome("Item 1");
        produto1.setDescricao("Desc 1");
        produto1.setPreco(BigDecimal.valueOf(10.0));
        produto1.setDisponivel(true);
        produto1.setCategoria("Comida");
        produto1.setRestaurante(restaurante1);
        entityManager.persist(produto1);

        produto2 = new Produto();
        produto2.setNome("Item 2");
        produto2.setDescricao("Desc 2");
        produto2.setPreco(BigDecimal.valueOf(5.0));
        produto2.setDisponivel(true);
        produto2.setCategoria("Bebida");
        produto2.setRestaurante(restaurante2);
        entityManager.persist(produto2);

        entityManager.flush(); // Garante que IDs são gerados para as entidades base

        // 4. Criar Itens de Pedido
        ItemPedido item1_pedido1 = new ItemPedido();
        item1_pedido1.setProduto(produto1);
        item1_pedido1.setQuantidade(2);
        item1_pedido1.setPrecoUnitario(produto1.getPreco());

        ItemPedido item2_pedido1 = new ItemPedido();
        item2_pedido1.setProduto(produto2);
        item2_pedido1.setQuantidade(1);
        item2_pedido1.setPrecoUnitario(produto2.getPreco());

        ItemPedido item1_pedido2 = new ItemPedido();
        item1_pedido2.setProduto(produto1);
        item1_pedido2.setQuantidade(1);
        item1_pedido2.setPrecoUnitario(produto1.getPreco());

        ItemPedido item1_pedido3 = new ItemPedido();
        item1_pedido3.setProduto(produto2);
        item1_pedido3.setQuantidade(3);
        item1_pedido3.setPrecoUnitario(produto2.getPreco());

        // 5. Criar e persistir Pedidos
        pedido1 = new Pedido();
        pedido1.setCliente(cliente1);
        pedido1.setRestaurante(restaurante1);
        pedido1.setEnderecoEntrega(new Endereco("Rua Principal", "10", "Centro", "São Paulo", "SP", "01000-000"));
        pedido1.setTotal(BigDecimal.valueOf(25.0)); // 2*10.0 + 1*5.0
        pedido1.setStatus(StatusPedido.CRIADO);
        pedido1.setDataPedido(LocalDateTime.of(2024, 1, 15, 10, 0));
        pedido1.setItens(Arrays.asList(item1_pedido1, item2_pedido1));
        entityManager.persist(pedido1);

        pedido2 = new Pedido();
        pedido2.setCliente(cliente1); // Mesmo cliente
        pedido2.setRestaurante(restaurante2);
        pedido2.setEnderecoEntrega(new Endereco("Av. Secundária", "20", "Vila Nova", "São Paulo", "SP", "02000-000"));
        pedido2.setTotal(BigDecimal.valueOf(10.0)); // 1*10.0
        pedido2.setStatus(StatusPedido.EM_PREPARACAO);
        pedido2.setDataPedido(LocalDateTime.of(2024, 1, 20, 14, 30));
        pedido2.setItens(Arrays.asList(item1_pedido2));
        entityManager.persist(pedido2);

        pedido3 = new Pedido();
        pedido3.setCliente(cliente2);
        pedido3.setRestaurante(restaurante1);
        pedido3.setEnderecoEntrega(new Endereco("Travessa da Paz", "30", "Jardim", "São Paulo", "SP", "03000-000"));
        pedido3.setTotal(BigDecimal.valueOf(15.0)); // 3*5.0
        pedido3.setStatus(StatusPedido.ENTREGUE);
        pedido3.setDataPedido(LocalDateTime.of(2024, 2, 1, 9, 0));
        pedido3.setItens(Arrays.asList(item1_pedido3));
        entityManager.persist(pedido3);

        entityManager.flush(); // Sincroniza o contexto de persistência com o banco de dados
    }

    // --- Testes para findByClienteId(Long clienteId) ---

    @Test
    @DisplayName("Deve encontrar pedidos por ID de cliente")
    void deveEncontrarPedidosPorClienteId() {
        // Act
        List<Pedido> pedidosCliente1 = pedidoRepository.findByClienteId(cliente1.getId());

        // Assert
        assertNotNull(pedidosCliente1);
        assertEquals(2, pedidosCliente1.size());
        assertTrue(pedidosCliente1.stream().anyMatch(p -> p.getId().equals(pedido1.getId())));
        assertTrue(pedidosCliente1.stream().anyMatch(p -> p.getId().equals(pedido2.getId())));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum pedido for encontrado para o ID do cliente")
    void deveRetornarListaVaziaParaClienteIdInexistente() {
        // Act
        List<Pedido> pedidosDeClienteInexistente = pedidoRepository.findByClienteId(999L); // ID que não existe

        // Assert
        assertNotNull(pedidosDeClienteInexistente);
        assertTrue(pedidosDeClienteInexistente.isEmpty());
    }

    // --- Testes para findByRestauranteId(Long restauranteId) ---

    @Test
    @DisplayName("Deve encontrar pedidos por ID de restaurante")
    void deveEncontrarPedidosPorRestauranteId() {
        // Act
        List<Pedido> pedidosRestaurante1 = pedidoRepository.findByRestauranteId(restaurante1.getId());

        // Assert
        assertNotNull(pedidosRestaurante1);
        assertEquals(2, pedidosRestaurante1.size());
        assertTrue(pedidosRestaurante1.stream().anyMatch(p -> p.getId().equals(pedido1.getId())));
        assertTrue(pedidosRestaurante1.stream().anyMatch(p -> p.getId().equals(pedido3.getId())));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se nenhum pedido for encontrado para o ID do restaurante")
    void deveRetornarListaVaziaParaRestauranteIdInexistente() {
        // Act
        List<Pedido> pedidosDeRestauranteInexistente = pedidoRepository.findByRestauranteId(999L); // ID que não existe

        // Assert
        assertNotNull(pedidosDeRestauranteInexistente);
        assertTrue(pedidosDeRestauranteInexistente.isEmpty());
    }

    // --- Testes para findByStatus(StatusPedido status) ---

    @Test
    @DisplayName("Deve encontrar pedidos por status específico")
    void deveEncontrarPedidosPorStatus() {
        // Act
        List<Pedido> pedidosCriados = pedidoRepository.findByStatus(StatusPedido.CRIADO);
        List<Pedido> pedidosEmPreparacao = pedidoRepository.findByStatus(StatusPedido.EM_PREPARACAO);
        List<Pedido> pedidosEntregues = pedidoRepository.findByStatus(StatusPedido.ENTREGUE);

        // Assert
        assertNotNull(pedidosCriados);
        assertEquals(1, pedidosCriados.size());
        assertEquals(pedido1.getId(), pedidosCriados.get(0).getId());

        assertNotNull(pedidosEmPreparacao);
        assertEquals(1, pedidosEmPreparacao.size());
        assertEquals(pedido2.getId(), pedidosEmPreparacao.get(0).getId());

        assertNotNull(pedidosEntregues);
        assertEquals(1, pedidosEntregues.size());
        assertEquals(pedido3.getId(), pedidosEntregues.get(0).getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver pedidos com o status")
    void deveRetornarListaVaziaParaStatusSemPedidos() {
        // Act
        List<Pedido> pedidosCancelados = pedidoRepository.findByStatus(StatusPedido.CANCELADO); // Não há pedidos cancelados

        // Assert
        assertNotNull(pedidosCancelados);
        assertTrue(pedidosCancelados.isEmpty());
    }

    // --- Testes para findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim) ---

    @Test
    @DisplayName("Deve encontrar pedidos dentro de uma faixa de data e hora")
    void deveEncontrarPedidosPorFaixaDeData() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2024, 1, 31, 23, 59);

        // Act
        List<Pedido> pedidosNoMesDeJaneiro = pedidoRepository.findByDataPedidoBetween(inicio, fim);

        // Assert
        assertNotNull(pedidosNoMesDeJaneiro);
        assertEquals(2, pedidosNoMesDeJaneiro.size());
        assertTrue(pedidosNoMesDeJaneiro.stream().anyMatch(p -> p.getId().equals(pedido1.getId())));
        assertTrue(pedidosNoMesDeJaneiro.stream().anyMatch(p -> p.getId().equals(pedido2.getId())));
        assertFalse(pedidosNoMesDeJaneiro.stream().anyMatch(p -> p.getId().equals(pedido3.getId()))); // Pedido 3 é de fevereiro
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver pedidos na faixa de data")
    void deveRetornarListaVaziaParaFaixaDeDataSemPedidos() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime fim = LocalDateTime.of(2025, 1, 31, 23, 59);

        // Act
        List<Pedido> pedidosNoFuturo = pedidoRepository.findByDataPedidoBetween(inicio, fim);

        // Assert
        assertNotNull(pedidosNoFuturo);
        assertTrue(pedidosNoFuturo.isEmpty());
    }

    // --- Testes para métodos herdados de JpaRepository (básicos) ---

    @Test
    @DisplayName("Deve salvar um pedido corretamente")
    void deveSalvarPedidoCorretamente() {
        // Arrange
        Pedido novoPedido = new Pedido();
        novoPedido.setCliente(cliente1);
        novoPedido.setRestaurante(restaurante1);
        novoPedido.setEnderecoEntrega(new Endereco("Nova Rua", "500", "Novo Bairro", "São Paulo", "SP", "04000-000"));
        novoPedido.setTotal(BigDecimal.valueOf(50.0));
        novoPedido.setStatus(StatusPedido.CRIADO);
        novoPedido.setDataPedido(LocalDateTime.now());
        novoPedido.setItens(Collections.emptyList()); // Pode ser vazio para teste de save

        // Act
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        // Assert
        assertNotNull(pedidoSalvo.getId());
        assertEquals("Nova Rua", pedidoSalvo.getEnderecoEntrega().getRua());
        assertEquals("500", pedidoSalvo.getEnderecoEntrega().getNumero());

        Pedido pedidoEncontrado = entityManager.find(Pedido.class, pedidoSalvo.getId());
        assertNotNull(pedidoEncontrado);
        assertEquals("Nova Rua", pedidoEncontrado.getEnderecoEntrega().getRua());
        assertEquals("500", pedidoEncontrado.getEnderecoEntrega().getNumero());
    }

    @Test
    @DisplayName("Deve encontrar pedido por ID existente")
    void deveEncontrarPedidoPorIdExistente() {
        // Act
        Optional<Pedido> foundPedido = pedidoRepository.findById(pedido1.getId());

        // Assert
        assertTrue(foundPedido.isPresent());
        assertEquals(pedido1.getEnderecoEntrega(), foundPedido.get().getEnderecoEntrega());
        assertEquals(pedido1.getStatus(), foundPedido.get().getStatus());
    }

    @Test
    @DisplayName("Não deve encontrar pedido por ID inexistente")
    void naoDeveEncontrarPedidoPorIdInexistente() {
        // Act
        Optional<Pedido> foundPedido = pedidoRepository.findById(999L);

        // Assert
        assertFalse(foundPedido.isPresent());
    }

    @Test
    @DisplayName("Deve retornar todos os pedidos")
    void deveRetornarTodosOsPedidos() {
        // Act
        List<Pedido> allPedidos = pedidoRepository.findAll();

        // Assert
        assertNotNull(allPedidos);
        assertEquals(3, allPedidos.size());
        assertTrue(allPedidos.stream().anyMatch(p -> p.getId().equals(pedido1.getId())));
        assertTrue(allPedidos.stream().anyMatch(p -> p.getId().equals(pedido2.getId())));
        assertTrue(allPedidos.stream().anyMatch(p -> p.getId().equals(pedido3.getId())));
    }

    @Test
    @DisplayName("Deve deletar um pedido por ID existente")
    void deveDeletarPedidoPorIdExistente() {
        // Arrange
        Long idToDelete = pedido1.getId();
        assertTrue(pedidoRepository.findById(idToDelete).isPresent());

        // Act
        pedidoRepository.deleteById(idToDelete);
        entityManager.flush();

        // Assert
        assertFalse(pedidoRepository.findById(idToDelete).isPresent());
    }

    @Test
    @DisplayName("Não deve lançar exceção ao tentar deletar pedido por ID inexistente")
    void naoDeveLancarExcecaoAoDeletarPedidoInexistente() {
        // Act & Assert
        assertDoesNotThrow(() -> pedidoRepository.deleteById(999L));
    }
}
