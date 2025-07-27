package com.deliverytech.api.repository;

import com.deliverytech.api.model.Cliente;
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
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteAtivo1;
    private Cliente clienteAtivo2;
    private Cliente clienteInativo;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // Criar clientes para os testes
        clienteAtivo1 = Cliente.builder()
                .nome("Alice Silva")
                .email("alice.silva@example.com")
                .ativo(true)
                .build();
        entityManager.persistAndFlush(clienteAtivo1);

        clienteAtivo2 = Cliente.builder()
                .nome("Bruno Costa")
                .email("bruno.costa@example.com")
                .ativo(true)
                .build();
        entityManager.persistAndFlush(clienteAtivo2);

        clienteInativo = Cliente.builder()
                .nome("Carla Dias")
                .email("carla.dias@example.com")
                .ativo(false)
                .build();
        entityManager.persistAndFlush(clienteInativo);
    }

    // --- Testes para findByEmail(String email) ---

    @Test
    @DisplayName("Deve encontrar cliente por email existente")
    void deveEncontrarClientePorEmailExistente() {
        // Act
        Optional<Cliente> foundCliente = clienteRepository.findByEmail("alice.silva@example.com");

        // Assert
        assertTrue(foundCliente.isPresent());
        assertEquals("Alice Silva", foundCliente.get().getNome());
        assertEquals("alice.silva@example.com", foundCliente.get().getEmail());
    }

    @Test
    @DisplayName("Não deve encontrar cliente por email inexistente")
    void naoDeveEncontrarClientePorEmailInexistente() {
        // Act
        Optional<Cliente> foundCliente = clienteRepository.findByEmail("naoexiste@example.com");

        // Assert
        assertFalse(foundCliente.isPresent());
    }

    // --- Testes para existsByEmail(String email) ---

    @Test
    @DisplayName("Deve retornar true para email existente")
    void deveRetornarTrueParaEmailExistente() {
        // Act
        boolean exists = clienteRepository.existsByEmail("bruno.costa@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve retornar false para email inexistente")
    void deveRetornarFalseParaEmailInexistente() {
        // Act
        boolean exists = clienteRepository.existsByEmail("outro@example.com");

        // Assert
        assertFalse(exists);
    }

    // --- Testes para findByAtivoTrue() ---

    @Test
    @DisplayName("Deve encontrar apenas clientes ativos")
    void deveEncontrarApenasClientesAtivos() {
        // Act
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();

        // Assert
        assertNotNull(clientesAtivos);
        assertEquals(2, clientesAtivos.size()); // alice.silva e bruno.costa
        assertTrue(clientesAtivos.stream().allMatch(Cliente::getAtivo));
        assertTrue(clientesAtivos.stream().anyMatch(c -> c.getEmail().equals("alice.silva@example.com")));
        assertTrue(clientesAtivos.stream().anyMatch(c -> c.getEmail().equals("bruno.costa@example.com")));
        assertFalse(clientesAtivos.stream().anyMatch(c -> c.getEmail().equals("carla.dias@example.com"))); // Carla está inativa
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver clientes ativos")
    void deveRetornarListaVaziaSeNaoHouverClientesAtivos() {
        // Arrange
        // Desativar todos os clientes existentes
        clienteAtivo1.setAtivo(false);
        clienteAtivo2.setAtivo(false);
        entityManager.merge(clienteAtivo1);
        entityManager.merge(clienteAtivo2);
        entityManager.flush();

        // Act
        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();

        // Assert
        assertNotNull(clientesAtivos);
        assertTrue(clientesAtivos.isEmpty());
    }

    // Teste de persistência básica (exemplo do modelo original, adaptado)
    @Test
    @DisplayName("Deve salvar cliente corretamente")
    void deveSalvarClienteCorretamente() {
        // Arrange
        Cliente novoCliente = Cliente.builder()
                .nome("Daniel Pereira")
                .email("daniel.pereira@example.com")
                .ativo(true)
                .build();

        // Act
        Cliente clienteSalvo = clienteRepository.save(novoCliente);

        // Assert
        assertNotNull(clienteSalvo.getId());
        assertEquals("Daniel Pereira", clienteSalvo.getNome());
        assertEquals("daniel.pereira@example.com", clienteSalvo.getEmail());
        assertTrue(clienteSalvo.getAtivo());

        Cliente clienteEncontrado = entityManager.find(Cliente.class, clienteSalvo.getId());
        assertNotNull(clienteEncontrado);
        assertEquals("Daniel Pereira", clienteEncontrado.getNome());
    }
}
