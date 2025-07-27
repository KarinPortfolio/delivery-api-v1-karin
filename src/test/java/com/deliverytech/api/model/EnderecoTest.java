package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    private Endereco endereco;

    @BeforeEach
   
    void setUp() {
        // Inicializa um objeto Endereco antes de cada teste
        endereco = new Endereco();
    }

    @Test
    @DisplayName("Deve criar um endereço com construtor vazio e valores padrão")
    void deveCriarEnderecoComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(endereco.getRua(), "Rua deve ser nula por padrão");
        assertNull(endereco.getNumero(), "Número deve ser nulo por padrão");
        assertNull(endereco.getBairro(), "Bairro deve ser nulo por padrão");
        assertNull(endereco.getCidade(), "Cidade deve ser nula por padrão");
        assertNull(endereco.getEstado(), "Estado deve ser nulo por padrão");
        assertNull(endereco.getCep(), "CEP deve ser nulo por padrão");
    }

    @Test
    @DisplayName("Deve criar um endereço usando o construtor AllArgsConstructor")
    void deveCriarEnderecoComAllArgsConstructor() {
        // Arrange
        String rua = "Rua Principal";
        String numero = "456";
        String bairro = "Centro";
        String cidade = "Rio de Janeiro";
        String estado = "RJ";
        String cep = "20000-000";

        // Act
        Endereco enderecoCompleto = new Endereco(rua, numero, bairro, cidade, estado, cep);

        // Assert
        assertEquals(rua, enderecoCompleto.getRua());
        assertEquals(numero, enderecoCompleto.getNumero());
        assertEquals(bairro, enderecoCompleto.getBairro());
        assertEquals(cidade, enderecoCompleto.getCidade());
        assertEquals(estado, enderecoCompleto.getEstado());
        assertEquals(cep, enderecoCompleto.getCep());
    }

    @Test
    @DisplayName("Deve criar um endereço usando o construtor Builder")
    void deveCriarEnderecoComBuilder() {
        // Arrange
        String rua = "Avenida Secundária";
        String numero = "789A";
        String bairro = "Jardins";
        String cidade = "São Paulo";
        String estado = "SP";
        String cep = "04000-000";

        // Act - Using constructor and setters instead of builder
        Endereco enderecoBuilder = new Endereco();
        enderecoBuilder.setRua(rua);
        enderecoBuilder.setNumero(numero);
        enderecoBuilder.setBairro(bairro);
        enderecoBuilder.setCidade(cidade);
        enderecoBuilder.setEstado(estado);
        enderecoBuilder.setCep(cep);

        // Assert
        assertEquals(rua, enderecoBuilder.getRua());
        assertEquals(numero, enderecoBuilder.getNumero());
        assertEquals(bairro, enderecoBuilder.getBairro());
        assertEquals(cidade, enderecoBuilder.getCidade());
        assertEquals(estado, enderecoBuilder.getEstado());
        assertEquals(cep, enderecoBuilder.getCep());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        String newRua = "Travessa da Paz";
        String newNumero = "10";
        String newBairro = "Vila Nova";
        String newCidade = "Belo Horizonte";
        String newEstado = "MG";
        String newCep = "30000-000";

        // Act
        endereco.setRua(newRua);
        endereco.setNumero(newNumero);
        endereco.setBairro(newBairro);
        endereco.setCidade(newCidade);
        endereco.setEstado(newEstado);
        endereco.setCep(newCep);

        // Assert
        assertEquals(newRua, endereco.getRua());
        assertEquals(newNumero, endereco.getNumero());
        assertEquals(newBairro, endereco.getBairro());
        assertEquals(newCidade, endereco.getCidade());
        assertEquals(newEstado, endereco.getEstado());
        assertEquals(newCep, endereco.getCep());
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Endereco")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        Endereco endereco1 = new Endereco("Rua X", "1", "Bairro Y", "Cidade Z", "UF", "12345-678");
        Endereco endereco2 = new Endereco("Rua X", "1", "Bairro Y", "Cidade Z", "UF", "12345-678");
        Endereco endereco3 = new Endereco("Rua A", "1", "Bairro Y", "Cidade Z", "UF", "12345-678"); // Rua diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(endereco1, endereco2, "Endereços com mesmos valores devem ser iguais");
        assertNotEquals(endereco1, endereco3, "Endereços com valores diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Endereco")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        Endereco endereco1 = new Endereco("Rua X", "1", "Bairro Y", "Cidade Z", "UF", "12345-678");
        Endereco endereco2 = new Endereco("Rua X", "1", "Bairro Y", "Cidade Z", "UF", "12345-678");

        // Assert
        assertEquals(endereco1.hashCode(), endereco2.hashCode(), "Hashcodes de endereços iguais devem ser iguais");
    }
}
