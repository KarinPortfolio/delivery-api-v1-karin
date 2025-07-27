package com.deliverytech.api.repository;

import com.deliverytech.api.model.Entrega;
import com.deliverytech.api.model.Entregador;
import com.deliverytech.api.model.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EntregaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EntregaRepository entregaRepository;

    private Entrega entrega1;
    private Entrega entrega2;
    private Pedido pedido1;
    private Pedido pedido2;
    private Entregador entregador1;
    private Entregador entregador2;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // Criar entidades relacionadas (Pedido e Entregador)
        pedido1 = new Pedido();
        pedido1.setEnderecoEntrega(new com.deliverytech.api.model.Endereco("Rua A", "100", "Centro", "São Paulo", "SP", "01234-567"));
        entityManager.persist(pedido1);

        pedido2 = new Pedido();
        pedido2.setEnderecoEntrega(new com.deliverytech.api.model.Endereco("Avenida B", "200", "Vila Nova", "São Paulo", "SP", "02345-678"));
        entityManager.persist(pedido2);

        entregador1 = new Entregador();
        entregador1.setNome("Entregador Alpha");
        entregador1.setEmail("alpha@delivery.com");
        entregador1.setCpf("12345678901");
        entregador1.setTelefone("11999999999");
        entregador1.setAtivo(true);
        entityManager.persist(entregador1);

        entregador2 = new Entregador();
        entregador2.setNome("Entregador Beta");
        entregador2.setEmail("beta@delivery.com");
        entregador2.setCpf("10987654321");
        entregador2.setTelefone("11888888888");
        entregador2.setAtivo(true);
        entityManager.persist(entregador2);

        // Criar e persistir entidades de Entrega
        entrega1 = new Entrega();
        entrega1.setPedido(pedido1);
        entrega1.setEntregador(entregador1);
        entrega1.setDataHoraEstimada(LocalDateTime.of(2025, 7, 26, 10, 0, 0));
        entityManager.persistAndFlush(entrega1);

        entrega2 = new Entrega();
        entrega2.setPedido(pedido2);
        entrega2.setEntregador(entregador2);
        entrega2.setDataHoraEstimada(LocalDateTime.of(2025, 7, 26, 11, 30, 0));
        entityManager.persistAndFlush(entrega2);
    }

    // --- Testes para métodos herdados de JpaRepository ---

    @Test
    @DisplayName("Deve salvar uma entrega corretamente")
    void deveSalvarEntregaCorretamente() {
        // Arrange
        Pedido novoPedido = new Pedido();
        novoPedido.setEnderecoEntrega(new com.deliverytech.api.model.Endereco("Rua C", "300", "Jardim das Flores", "São Paulo", "SP", "03456-789"));
        novoPedido.setDataPedido(LocalDateTime.now());
        entityManager.persist(novoPedido);

        Entregador novoEntregador = new Entregador();
        novoEntregador.setNome("Entregador Gama");
        novoEntregador.setEmail("gama@delivery.com");
        novoEntregador.setCpf("11122233344");
        novoEntregador.setTelefone("11777777777");
        novoEntregador.setAtivo(true);
        entityManager.persist(novoEntregador);
        entityManager.flush(); // Garante que as entidades relacionadas estão persistidas

        Entrega novaEntrega = new Entrega();
        novaEntrega.setPedido(novoPedido);
        novaEntrega.setEntregador(novoEntregador);
        novaEntrega.setDataHoraEstimada(LocalDateTime.of(2025, 7, 26, 14, 0, 0));

        // Act
        Entrega entregaSalva = entregaRepository.save(novaEntrega);

        // Assert
        assertNotNull(entregaSalva.getId());
        assertEquals(novoPedido.getId(), entregaSalva.getPedido().getId());
        assertEquals(novoEntregador.getId(), entregaSalva.getEntregador().getId());

        // Verificar se foi salvo no banco de dados
        Entrega entregaEncontrada = entityManager.find(Entrega.class, entregaSalva.getId());
        assertNotNull(entregaEncontrada);
    }

    @Test
    @DisplayName("Deve encontrar entrega por ID existente")
    void deveEncontrarEntregaPorIdExistente() {
        // Act
        Optional<Entrega> foundEntrega = entregaRepository.findById(entrega1.getId());

        // Assert
        assertTrue(foundEntrega.isPresent());
        assertEquals(pedido1.getId(), foundEntrega.get().getPedido().getId());
        assertEquals(entregador1.getId(), foundEntrega.get().getEntregador().getId());
    }

    @Test
    @DisplayName("Não deve encontrar entrega por ID inexistente")
    void naoDeveEncontrarEntregaPorIdInexistente() {
        // Act
        Optional<Entrega> foundEntrega = entregaRepository.findById(999L); // ID que não existe

        // Assert
        assertFalse(foundEntrega.isPresent());
    }

    @Test
    @DisplayName("Deve retornar todas as entregas")
    void deveRetornarTodasAsEntregas() {
        // Act
        List<Entrega> allEntregas = entregaRepository.findAll();

        // Assert
        assertNotNull(allEntregas);
        assertEquals(2, allEntregas.size());
        assertTrue(allEntregas.stream().anyMatch(e -> e.getId().equals(entrega1.getId())));
        assertTrue(allEntregas.stream().anyMatch(e -> e.getId().equals(entrega2.getId())));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver entregas")
    void deveRetornarListaVaziaSeNaoHouverEntregas() {
        // Arrange
        entityManager.clear(); // Limpa as entregas persistidas no setUp
        entregaRepository.deleteAll(); // Garante que não há entregas no banco antes do teste

        // Act
        List<Entrega> allEntregas = entregaRepository.findAll();

        // Assert
        assertNotNull(allEntregas);
        assertTrue(allEntregas.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar uma entrega por ID existente")
    void deveDeletarEntregaPorIdExistente() {
        // Arrange
        Long idToDelete = entrega1.getId();
        assertTrue(entregaRepository.findById(idToDelete).isPresent()); // Verifica que existe antes de deletar

        // Act
        entregaRepository.deleteById(idToDelete);
        entityManager.flush(); // Garante que a operação de delete foi sincronizada

        // Assert
        assertFalse(entregaRepository.findById(idToDelete).isPresent()); // Verifica que não existe mais
    }

    @Test
    @DisplayName("Não deve lançar exceção ao tentar deletar entrega por ID inexistente")
    void naoDeveLancarExcecaoAoDeletarEntregaInexistente() {
        // Act & Assert
        assertDoesNotThrow(() -> entregaRepository.deleteById(999L)); // Tenta deletar um ID que não existe
        // O JpaRepository.deleteById não lança exceção se o ID não for encontrado.
    }
}
