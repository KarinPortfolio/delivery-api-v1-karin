package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        // Inicializa um objeto Restaurante antes de cada teste
        restaurante = new Restaurante();
    }

    @Test
    @DisplayName("Deve criar um restaurante com construtor vazio e valores padrão")
    void deveCriarRestauranteComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(restaurante.getId(), "ID deve ser nulo para um novo restaurante sem ID definido");
        assertNull(restaurante.getNome(), "Nome deve ser nulo por padrão");
        assertNull(restaurante.getTelefone(), "Telefone deve ser nulo por padrão");
        assertNull(restaurante.getCategoria(), "Categoria deve ser nula por padrão");
        assertNull(restaurante.getTaxaEntrega(), "Taxa de entrega deve ser nula por padrão");
        assertNull(restaurante.getTempoEntregaMinutos(), "Tempo de entrega deve ser nulo por padrão");
        assertTrue(restaurante.getAtivo(), "Ativo deve ser true por padrão");
        assertNotNull(restaurante.getDataCadastro(), "Data de cadastro não deve ser nula por padrão");
        assertTrue(restaurante.getDataCadastro().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de cadastro deve ser aproximadamente agora");
    }

    @Test
    @DisplayName("Deve criar um restaurante usando o construtor com parâmetros")
    void deveCriarRestauranteComConstrutorParametros() {
        // Arrange
        Long id = 1L;
        String nome = "Pizzaria Deliciosa";
        String categoria = "Pizza";
        String telefone = "551133334444";
        BigDecimal taxaEntrega = new BigDecimal("5.00");
        Integer tempoEntregaMinutos = 30;
        Boolean ativo = true;
        LocalDateTime dataCadastro = LocalDateTime.of(2024, 1, 1, 12, 0);

        // Act
        Restaurante restauranteCompleto = new Restaurante(id, nome, categoria, telefone, taxaEntrega, tempoEntregaMinutos, ativo, dataCadastro);

        // Assert
        assertEquals(id, restauranteCompleto.getId());
        assertEquals(nome, restauranteCompleto.getNome());
        assertEquals(categoria, restauranteCompleto.getCategoria());
        assertEquals(telefone, restauranteCompleto.getTelefone());
        assertEquals(taxaEntrega, restauranteCompleto.getTaxaEntrega());
        assertEquals(tempoEntregaMinutos, restauranteCompleto.getTempoEntregaMinutos());
        assertEquals(ativo, restauranteCompleto.getAtivo());
        assertEquals(dataCadastro, restauranteCompleto.getDataCadastro());
    }

    @Test
    @DisplayName("Deve criar um restaurante usando setters")
    void deveCriarRestauranteComSetters() {
        // Arrange
        Long id = 2L;
        String nome = "Hamburgueria Top";
        String categoria = "Hamburger";
        String telefone = "551144445555";
        BigDecimal taxaEntrega = new BigDecimal("7.50");
        Integer tempoEntregaMinutos = 45;
        Boolean ativo = false;
        LocalDateTime dataCadastro = LocalDateTime.of(2023, 5, 10, 9, 30);

        // Act
        restaurante.setId(id);
        restaurante.setNome(nome);
        restaurante.setCategoria(categoria);
        restaurante.setTelefone(telefone);
        restaurante.setTaxaEntrega(taxaEntrega);
        restaurante.setTempoEntregaMinutos(tempoEntregaMinutos);
        restaurante.setAtivo(ativo);
        restaurante.setDataCadastro(dataCadastro);

        // Assert
        assertEquals(id, restaurante.getId());
        assertEquals(nome, restaurante.getNome());
        assertEquals(categoria, restaurante.getCategoria());
        assertEquals(telefone, restaurante.getTelefone());
        assertEquals(taxaEntrega, restaurante.getTaxaEntrega());
        assertEquals(tempoEntregaMinutos, restaurante.getTempoEntregaMinutos());
        assertEquals(ativo, restaurante.getAtivo());
        assertEquals(dataCadastro, restaurante.getDataCadastro());
    }

    @Test
    @DisplayName("Deve testar os getters e setters")
    void deveTestarGettersSetters() {
        // Arrange
        Long newId = 3L;
        String newNome = "Sushi House";
        String newCategoria = "Japonesa";
        String newTelefone = "551155556666";
        BigDecimal newTaxaEntrega = new BigDecimal("12.00");
        Integer newTempoEntrega = 60;
        Boolean newAtivo = true;
        LocalDateTime newDataCadastro = LocalDateTime.of(2022, 11, 20, 15, 45);

        // Act
        restaurante.setId(newId);
        restaurante.setNome(newNome);
        restaurante.setCategoria(newCategoria);
        restaurante.setTelefone(newTelefone);
        restaurante.setTaxaEntrega(newTaxaEntrega);
        restaurante.setTempoEntregaMinutos(newTempoEntrega);
        restaurante.setAtivo(newAtivo);
        restaurante.setDataCadastro(newDataCadastro);

        // Assert
        assertEquals(newId, restaurante.getId());
        assertEquals(newNome, restaurante.getNome());
        assertEquals(newCategoria, restaurante.getCategoria());
        assertEquals(newTelefone, restaurante.getTelefone());
        assertEquals(newTaxaEntrega, restaurante.getTaxaEntrega());
        assertEquals(newTempoEntrega, restaurante.getTempoEntregaMinutos());
        assertEquals(newAtivo, restaurante.getAtivo());
        assertEquals(newDataCadastro, restaurante.getDataCadastro());
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'ativo'")
    void deveVerificarAtivoPadrao() {
        // Act
        Restaurante novoRestaurante = new Restaurante();

        // Assert
        assertTrue(novoRestaurante.getAtivo(), "Ativo deve ser true por padrão");
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'dataCadastro'")
    void deveVerificarDataCadastroPadrao() {
        // Act
        Restaurante novoRestaurante = new Restaurante();

        // Assert
        assertNotNull(novoRestaurante.getDataCadastro(), "Data de cadastro não deve ser nula por padrão");
        assertTrue(novoRestaurante.getDataCadastro().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de cadastro deve ser aproximadamente agora");
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Restaurante")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        BigDecimal taxa = new BigDecimal("5.00");
        
        Restaurante restaurante1 = new Restaurante(1L, "Pizza Place", "Pizza", "551133334444", taxa, 30, true, now);
        Restaurante restaurante2 = new Restaurante(1L, "Pizza Place", "Pizza", "551133334444", taxa, 30, true, now);
        Restaurante restaurante3 = new Restaurante(2L, "Pizza Place", "Pizza", "551133334444", taxa, 30, true, now); // ID diferente

        // Assert (assumindo que equals foi implementado corretamente)
        assertEquals(restaurante1, restaurante2, "Restaurantes com mesmos valores devem ser iguais");
        assertNotEquals(restaurante1, restaurante3, "Restaurantes com IDs diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Restaurante")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        BigDecimal taxa = new BigDecimal("5.00");
        
        Restaurante restaurante1 = new Restaurante(1L, "Pizza Place", "Pizza", "551133334444", taxa, 30, true, now);
        Restaurante restaurante2 = new Restaurante(1L, "Pizza Place", "Pizza", "551133334444", taxa, 30, true, now);

        // Assert
        assertEquals(restaurante1.hashCode(), restaurante2.hashCode(), "Hashcodes de restaurantes iguais devem ser iguais");
    }
}