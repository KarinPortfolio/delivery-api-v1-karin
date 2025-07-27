package com.deliverytech.api.repository;

import com.deliverytech.api.model.Entregador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EntregadorRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EntregadorRepository entregadorRepository;

    private Entregador entregador1;
    private Entregador entregador2;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // Criar e persistir entregadores para os testes
        entregador1 = new Entregador();
        entregador1.setNome("Carlos Entregador");
        entregador1.setEmail("carlos.e@example.com");
        entregador1.setCpf("111.222.333-44");
        entregador1.setTelefone("5511999991111");
        entregador1.setAtivo(true);
        entityManager.persistAndFlush(entregador1);

        entregador2 = new Entregador();
        entregador2.setNome("Fernanda Entregadora");
        entregador2.setEmail("fernanda.e@example.com");
        entregador2.setCpf("555.666.777-88");
        entregador2.setTelefone("5511999992222");
        entregador2.setAtivo(true);
        entityManager.persistAndFlush(entregador2);
    }

    // --- Testes para métodos herdados de JpaRepository ---

    @Test
    @DisplayName("Deve salvar um entregador corretamente")
    void deveSalvarEntregadorCorretamente() {
        // Arrange
        Entregador novoEntregador = new Entregador();
        novoEntregador.setNome("Novo Entregador");
        novoEntregador.setEmail("novo.e@example.com");
        novoEntregador.setCpf("000.111.222-33");
        novoEntregador.setTelefone("5511999993333");
        novoEntregador.setAtivo(false);

        // Act
        Entregador entregadorSalvo = entregadorRepository.save(novoEntregador);

        // Assert
        assertNotNull(entregadorSalvo.getId());
        assertEquals("Novo Entregador", entregadorSalvo.getNome());
        assertEquals("novo.e@example.com", entregadorSalvo.getEmail());
        assertFalse(entregadorSalvo.getAtivo());

        // Verificar se foi salvo no banco de dados
        Entregador entregadorEncontrado = entityManager.find(Entregador.class, entregadorSalvo.getId());
        assertNotNull(entregadorEncontrado);
        assertEquals("Novo Entregador", entregadorEncontrado.getNome());
    }

    @Test
    @DisplayName("Deve encontrar entregador por ID existente")
    void deveEncontrarEntregadorPorIdExistente() {
        // Act
        Optional<Entregador> foundEntregador = entregadorRepository.findById(entregador1.getId());

        // Assert
        assertTrue(foundEntregador.isPresent());
        assertEquals("Carlos Entregador", foundEntregador.get().getNome());
        assertEquals("carlos.e@example.com", foundEntregador.get().getEmail());
    }

    @Test
    @DisplayName("Não deve encontrar entregador por ID inexistente")
    void naoDeveEncontrarEntregadorPorIdInexistente() {
        // Act
        Optional<Entregador> foundEntregador = entregadorRepository.findById(999L); // ID que não existe

        // Assert
        assertFalse(foundEntregador.isPresent());
    }

    @Test
    @DisplayName("Deve retornar todos os entregadores")
    void deveRetornarTodosOsEntregadores() {
        // Act
        List<Entregador> allEntregadores = entregadorRepository.findAll();

        // Assert
        assertNotNull(allEntregadores);
        assertEquals(2, allEntregadores.size());
        assertTrue(allEntregadores.stream().anyMatch(e -> e.getNome().equals("Carlos Entregador")));
        assertTrue(allEntregadores.stream().anyMatch(e -> e.getNome().equals("Fernanda Entregadora")));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver entregadores")
    void deveRetornarListaVaziaSeNaoHouverEntregadores() {
        // Arrange
        entityManager.clear(); // Limpa os entregadores persistidos no setUp
        entregadorRepository.deleteAll(); // Garante que não há entregadores no banco antes do teste

        // Act
        List<Entregador> allEntregadores = entregadorRepository.findAll();

        // Assert
        assertNotNull(allEntregadores);
        assertTrue(allEntregadores.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um entregador por ID existente")
    void deveDeletarEntregadorPorIdExistente() {
        // Arrange
        Long idToDelete = entregador1.getId();
        assertTrue(entregadorRepository.findById(idToDelete).isPresent()); // Verifica que existe antes de deletar

        // Act
        entregadorRepository.deleteById(idToDelete);
        entityManager.flush(); // Garante que a operação de delete foi sincronizada

        // Assert
        assertFalse(entregadorRepository.findById(idToDelete).isPresent()); // Verifica que não existe mais
    }

    @Test
    @DisplayName("Não deve lançar exceção ao tentar deletar entregador por ID inexistente")
    void naoDeveLancarExcecaoAoDeletarEntregadorInexistente() {
        // Act & Assert
        assertDoesNotThrow(() -> entregadorRepository.deleteById(999L)); // Tenta deletar um ID que não existe
        // Não há uma forma direta de verificar se o delete foi *tentado* em um mock sem lançar exceção,
        // mas o comportamento esperado é que não haja erro para IDs não encontrados.
    }
}