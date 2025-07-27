package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntregadorTest {

    private Entregador entregador;

    @BeforeEach
    void setUp() {
        // Inicializa um objeto Entregador antes de cada teste
        entregador = new Entregador();
    }

    @Test
    @DisplayName("Deve criar um entregador com construtor vazio e valores padrão")
    void deveCriarEntregadorComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(entregador.getId(), "ID deve ser nulo para um novo entregador sem ID definido");
        assertNull(entregador.getNome(), "Nome deve ser nulo por padrão");
        assertNull(entregador.getEmail(), "Email deve ser nulo por padrão");
        assertNull(entregador.getCpf(), "CPF deve ser nulo por padrão");
        assertNull(entregador.getTelefone(), "Telefone deve ser nulo por padrão");
        assertTrue(entregador.getAtivo(), "Ativo deve ser true por padrão (@Builder.Default)");
        assertNotNull(entregador.getDataCriacao(), "Data de criação não deve ser nula por padrão (@Builder.Default)");
        assertTrue(entregador.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de criação deve ser aproximadamente agora");
        assertNull(entregador.getEntregas(), "Lista de entregas deve ser nula por padrão");
    }

    @Test
    @DisplayName("Deve criar um entregador usando o construtor AllArgsConstructor")
    void deveCriarEntregadorComAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String nome = "João Silva";
        String email = "joao.silva@example.com";
        String cpf = "123.456.789-00";
        String telefone = "5511987654321";
        Boolean ativo = true;
        LocalDateTime dataCriacao = LocalDateTime.of(2024, 1, 1, 10, 0);
        List<Entrega> entregas = new ArrayList<>(); // Pode ser uma lista vazia ou com objetos Entrega mockados

        // Act
        Entregador entregadorCompleto = new Entregador(id, nome, email, cpf, telefone, ativo, dataCriacao, entregas);

        // Assert
        assertEquals(id, entregadorCompleto.getId());
        assertEquals(nome, entregadorCompleto.getNome());
        assertEquals(email, entregadorCompleto.getEmail());
        assertEquals(cpf, entregadorCompleto.getCpf());
        assertEquals(telefone, entregadorCompleto.getTelefone());
        assertEquals(ativo, entregadorCompleto.getAtivo());
        assertEquals(dataCriacao, entregadorCompleto.getDataCriacao());
        assertEquals(entregas, entregadorCompleto.getEntregas());
    }

    @Test
    @DisplayName("Deve criar um entregador usando o construtor Builder")
    void deveCriarEntregadorComBuilder() {
        // Arrange
        Long id = 2L;
        String nome = "Maria Oliveira";
        String email = "maria.o@example.com";
        String cpf = "987.654.321-00";
        String telefone = "5511998877665";

        // Act
        Entregador entregadorBuilder = new Entregador();
        entregadorBuilder.setId(id);
        entregadorBuilder.setNome(nome);
        entregadorBuilder.setEmail(email);
        entregadorBuilder.setCpf(cpf);
        entregadorBuilder.setTelefone(telefone);
        entregadorBuilder.setAtivo(false); // Explicitamente definido
        entregadorBuilder.setDataCriacao(LocalDateTime.of(2023, 5, 10, 9, 30)); // Explicitamente definido
        entregadorBuilder.setEntregas(new ArrayList<>()); // Explicitamente definida

        // Assert
        assertEquals(id, entregadorBuilder.getId());
        assertEquals(nome, entregadorBuilder.getNome());
        assertEquals(email, entregadorBuilder.getEmail());
        assertEquals(cpf, entregadorBuilder.getCpf());
        assertEquals(telefone, entregadorBuilder.getTelefone());
        assertFalse(entregadorBuilder.getAtivo());
        assertEquals(LocalDateTime.of(2023, 5, 10, 9, 30), entregadorBuilder.getDataCriacao());
        assertNotNull(entregadorBuilder.getEntregas());
        assertTrue(entregadorBuilder.getEntregas().isEmpty());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        Long newId = 3L;
        String newName = "Pedro Santos";
        String newEmail = "pedro.santos@example.com";
        String newCpf = "333.222.111-00";
        String newTelefone = "5511977776666";
        Boolean newAtivo = false;
        LocalDateTime newDataCriacao = LocalDateTime.of(2022, 11, 20, 15, 45);
        List<Entrega> newEntregas = new ArrayList<>();
        // Adicionar uma entrega de exemplo (mock ou stub)
        Entrega entregaExemplo = new Entrega(); // Supondo que Entrega tem um construtor vazio
        newEntregas.add(entregaExemplo);

        // Act
        entregador.setId(newId);
        entregador.setNome(newName);
        entregador.setEmail(newEmail);
        entregador.setCpf(newCpf);
        entregador.setTelefone(newTelefone);
        entregador.setAtivo(newAtivo);
        entregador.setDataCriacao(newDataCriacao);
        entregador.setEntregas(newEntregas);

        // Assert
        assertEquals(newId, entregador.getId());
        assertEquals(newName, entregador.getNome());
        assertEquals(newEmail, entregador.getEmail());
        assertEquals(newCpf, entregador.getCpf());
        assertEquals(newTelefone, entregador.getTelefone());
        assertEquals(newAtivo, entregador.getAtivo());
        assertEquals(newDataCriacao, entregador.getDataCriacao());
        assertEquals(newEntregas, entregador.getEntregas());
        assertFalse(entregador.getEntregas().isEmpty());
        assertEquals(1, entregador.getEntregas().size());
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'ativo' quando não definido no builder")
    void deveVerificarAtivoPadraoNoBuilder() {
        // Act
        Entregador entregadorSemAtivo = new Entregador();
        entregadorSemAtivo.setNome("Entregador Sem Ativo");
        entregadorSemAtivo.setEmail("sem.ativo@example.com");
        entregadorSemAtivo.setCpf("111.111.111-11");
        entregadorSemAtivo.setTelefone("5511111111111");

        // Assert
        assertTrue(entregadorSemAtivo.getAtivo(), "Ativo deve ser true por padrão quando não especificado no builder");
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'dataCriacao' quando não definido no builder")
    void deveVerificarDataCriacaoPadraoNoBuilder() {
        // Act
        Entregador entregadorSemDataCriacao = new Entregador();
        entregadorSemDataCriacao.setNome("Entregador Sem Data Criação");
        entregadorSemDataCriacao.setEmail("sem.data@example.com");
        entregadorSemDataCriacao.setCpf("222.222.222-22");
        entregadorSemDataCriacao.setTelefone("5511222222222");

        // Assert
        assertNotNull(entregadorSemDataCriacao.getDataCriacao(), "Data de criação não deve ser nula quando não especificada no builder");
        assertTrue(entregadorSemDataCriacao.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de criação deve ser aproximadamente agora");
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Entregador")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Entregador entregador1 = new Entregador(1L, "Nome", "email@test.com", "123", "tel", true, now, new ArrayList<>());
        Entregador entregador2 = new Entregador(1L, "Nome", "email@test.com", "123", "tel", true, now, new ArrayList<>());
        Entregador entregador3 = new Entregador(2L, "Nome", "email@test.com", "123", "tel", true, now, new ArrayList<>()); // ID diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(entregador1, entregador2, "Entregadores com mesmos valores devem ser iguais");
        assertNotEquals(entregador1, entregador3, "Entregadores com IDs diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Entregador")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Entregador entregador1 = new Entregador(1L, "Nome", "email@test.com", "123", "tel", true, now, new ArrayList<>());
        Entregador entregador2 = new Entregador(1L, "Nome", "email@test.com", "123", "tel", true, now, new ArrayList<>());

        // Assert
        assertEquals(entregador1.hashCode(), entregador2.hashCode(), "Hashcodes de entregadores iguais devem ser iguais");
    }
}
