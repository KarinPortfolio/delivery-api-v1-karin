package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Inicializa um objeto Cliente antes de cada teste
        cliente = new Cliente();
    }

    @Test
    @DisplayName("Deve criar um cliente com construtor vazio e valores padrão")
    void deveCriarClienteComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(cliente.getId(), "ID deve ser nulo para um novo cliente sem ID definido");
        assertNull(cliente.getNome(), "Nome deve ser nulo por padrão");
        assertNull(cliente.getEmail(), "Email deve ser nulo por padrão");
        assertTrue(cliente.getAtivo(), "Ativo deve ser true por padrão (@Builder.Default)");
        assertNotNull(cliente.getDataCadastro(), "Data de cadastro não deve ser nula por padrão (@Builder.Default)");
        assertTrue(cliente.getDataCadastro().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de cadastro deve ser aproximadamente agora");
    }

    @Test
    @DisplayName("Deve criar um cliente usando o construtor AllArgsConstructor")
    void deveCriarClienteComAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String nome = "João da Silva";
        String email = "joao.silva@example.com";
        Boolean ativo = false;
        LocalDateTime dataCadastro = LocalDateTime.of(2024, 1, 1, 12, 0);

        // Act
        Cliente clienteCompleto = new Cliente(id, nome, email, ativo, dataCadastro);

        // Assert
        assertEquals(id, clienteCompleto.getId());
        assertEquals(nome, clienteCompleto.getNome());
        assertEquals(email, clienteCompleto.getEmail());
        assertEquals(ativo, clienteCompleto.getAtivo());
        assertEquals(dataCadastro, clienteCompleto.getDataCadastro());
    }

    @Test
    @DisplayName("Deve criar um cliente usando o construtor Builder")
    void deveCriarClienteComBuilder() {
        // Arrange
        Long id = 2L;
        String nome = "Maria Souza";
        String email = "maria.souza@example.com";

        // Act
        Cliente clienteBuilder = Cliente.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .ativo(true) // Explicitamente definido
                .dataCadastro(LocalDateTime.of(2023, 5, 10, 9, 30)) // Explicitamente definido
                .build();

        // Assert
        assertEquals(id, clienteBuilder.getId());
        assertEquals(nome, clienteBuilder.getNome());
        assertEquals(email, clienteBuilder.getEmail());
        assertTrue(clienteBuilder.getAtivo());
        assertEquals(LocalDateTime.of(2023, 5, 10, 9, 30), clienteBuilder.getDataCadastro());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        Long newId = 3L;
        String newName = "Pedro Santos";
        String newEmail = "pedro.santos@example.com";
        Boolean newAtivo = false;
        LocalDateTime newDataCadastro = LocalDateTime.of(2022, 11, 20, 15, 45);

        // Act
        cliente.setId(newId);
        cliente.setNome(newName);
        cliente.setEmail(newEmail);
        cliente.setAtivo(newAtivo);
        cliente.setDataCadastro(newDataCadastro);

        // Assert
        assertEquals(newId, cliente.getId());
        assertEquals(newName, cliente.getNome());
        assertEquals(newEmail, cliente.getEmail());
        assertEquals(newAtivo, cliente.getAtivo());
        assertEquals(newDataCadastro, cliente.getDataCadastro());
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'ativo' quando não definido no builder")
    void deveVerificarAtivoPadraoNoBuilder() {
        // Act
        Cliente clienteSemAtivo = Cliente.builder()
                .nome("Cliente Sem Ativo")
                .email("sem.ativo@example.com")
                .build();

        // Assert
        assertTrue(clienteSemAtivo.getAtivo(), "Ativo deve ser true por padrão quando não especificado no builder");
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'dataCadastro' quando não definido no builder")
    void deveVerificarDataCadastroPadraoNoBuilder() {
        // Act
        Cliente clienteSemDataCadastro = Cliente.builder()
                .nome("Cliente Sem Data Cadastro")
                .email("sem.data@example.com")
                .build();

        // Assert
        assertNotNull(clienteSemDataCadastro.getDataCadastro(), "Data de cadastro não deve ser nula quando não especificada no builder");
        assertTrue(clienteSemDataCadastro.getDataCadastro().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de cadastro deve ser aproximadamente agora");
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Cliente")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Cliente cliente1 = new Cliente(1L, "Test", "test@example.com", true, now);
        Cliente cliente2 = new Cliente(1L, "Test", "test@example.com", true, now);
        Cliente cliente3 = new Cliente(2L, "Test", "test@example.com", true, now); // ID diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(cliente1, cliente2, "Clientes com mesmos valores devem ser iguais");
        assertNotEquals(cliente1, cliente3, "Clientes com IDs diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Cliente")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Cliente cliente1 = new Cliente(1L, "Test", "test@example.com", true, now);
        Cliente cliente2 = new Cliente(1L, "Test", "test@example.com", true, now);

        // Assert
        assertEquals(cliente1.hashCode(), cliente2.hashCode(), "Hashcodes de clientes iguais devem ser iguais");
    }
}