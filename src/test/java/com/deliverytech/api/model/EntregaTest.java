package com.deliverytech.api.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EntregaTest {

    private Entrega entrega;
    private Pedido pedido;
    private Entregador entregador;
    private LocalDateTime dataHoraEstimada;
    private LocalDateTime dataHoraRealizada;

    @BeforeEach
    void setUp() {
        // Criar objetos auxiliares
        pedido = new Pedido();
        pedido.setId(1L);

        entregador = new Entregador();
        entregador.setId(1L);
        entregador.setNome("João Entregador");

        dataHoraEstimada = LocalDateTime.of(2025, 7, 25, 12, 0);
        dataHoraRealizada = LocalDateTime.of(2025, 7, 25, 11, 55);

        // Criar entrega de teste
        entrega = new Entrega();
    }

    @Test
    @DisplayName("Deve criar entrega com construtor padrão")
    void deveCriarEntregaComConstrutorPadrao() {
        // Act
        Entrega novaEntrega = new Entrega();

        // Assert
        assertNotNull(novaEntrega);
        assertNull(novaEntrega.getId());
        assertNull(novaEntrega.getPedido());
        assertNull(novaEntrega.getEntregador());
        assertNull(novaEntrega.getStatus());
        assertNull(novaEntrega.getEnderecoEntrega());
        assertNull(novaEntrega.getDataHoraEstimada());
        assertNull(novaEntrega.getDataHoraRealizada());
        assertNull(novaEntrega.getCustoEntrega());
    }

    @Test
    @DisplayName("Deve criar entrega com construtor completo")
    void deveCriarEntregaComConstrutorCompleto() {
        // Arrange
        Long id = 1L;
        StatusEntrega status = StatusEntrega.EM_ANDAMENTO;
        String endereco = "Rua das Flores, 123";
        BigDecimal custo = new BigDecimal("8.50");

        // Act
        Entrega novaEntrega = new Entrega(id, pedido, entregador, status, endereco, 
                                         dataHoraEstimada, dataHoraRealizada, custo);

        // Assert
        assertEquals(id, novaEntrega.getId());
        assertEquals(pedido, novaEntrega.getPedido());
        assertEquals(entregador, novaEntrega.getEntregador());
        assertEquals(status, novaEntrega.getStatus());
        assertEquals(endereco, novaEntrega.getEnderecoEntrega());
        assertEquals(dataHoraEstimada, novaEntrega.getDataHoraEstimada());
        assertEquals(dataHoraRealizada, novaEntrega.getDataHoraRealizada());
        assertEquals(custo, novaEntrega.getCustoEntrega());
    }

    @Test
    @DisplayName("Deve definir e obter ID corretamente")
    void deveDefinirEObterIdCorretamente() {
        // Arrange
        Long id = 10L;

        // Act
        entrega.setId(id);

        // Assert
        assertEquals(id, entrega.getId());
    }

    @Test
    @DisplayName("Deve definir e obter pedido corretamente")
    void deveDefinirEObterPedidoCorretamente() {
        // Act
        entrega.setPedido(pedido);

        // Assert
        assertEquals(pedido, entrega.getPedido());
    }

    @Test
    @DisplayName("Deve definir e obter entregador corretamente")
    void deveDefinirEObterEntregadorCorretamente() {
        // Act
        entrega.setEntregador(entregador);

        // Assert
        assertEquals(entregador, entrega.getEntregador());
    }

    @Test
    @DisplayName("Deve definir e obter status corretamente")
    void deveDefinirEObterStatusCorretamente() {
        // Arrange
        StatusEntrega status = StatusEntrega.ENTREGUE;

        // Act
        entrega.setStatus(status);

        // Assert
        assertEquals(status, entrega.getStatus());
    }

    @Test
    @DisplayName("Deve definir e obter endereço de entrega corretamente")
    void deveDefinirEObterEnderecoEntregaCorretamente() {
        // Arrange
        String endereco = "Av. Principal, 456 - Centro";

        // Act
        entrega.setEnderecoEntrega(endereco);

        // Assert
        assertEquals(endereco, entrega.getEnderecoEntrega());
    }

    @Test
    @DisplayName("Deve definir e obter data/hora estimada corretamente")
    void deveDefinirEObterDataHoraEstimadaCorretamente() {
        // Act
        entrega.setDataHoraEstimada(dataHoraEstimada);

        // Assert
        assertEquals(dataHoraEstimada, entrega.getDataHoraEstimada());
    }

    @Test
    @DisplayName("Deve definir e obter data/hora realizada corretamente")
    void deveDefinirEObterDataHoraRealizadaCorretamente() {
        // Act
        entrega.setDataHoraRealizada(dataHoraRealizada);

        // Assert
        assertEquals(dataHoraRealizada, entrega.getDataHoraRealizada());
    }

    @Test
    @DisplayName("Deve definir e obter custo de entrega corretamente")
    void deveDefinirEObterCustoEntregaCorretamente() {
        // Arrange
        BigDecimal custo = new BigDecimal("12.75");

        // Act
        entrega.setCustoEntrega(custo);

        // Assert
        assertEquals(custo, entrega.getCustoEntrega());
    }

    @Test
    @DisplayName("Deve aceitar custo de entrega nulo")
    void deveAceitarCustoEntregaNulo() {
        // Act
        entrega.setCustoEntrega(null);

        // Assert
        assertNull(entrega.getCustoEntrega());
    }

    @Test
    @DisplayName("Deve implementar equals corretamente para objetos iguais")
    void deveImplementarEqualsCorretamenteParaObjetosIguais() {
        // Arrange
        Entrega entrega1 = criarEntregaCompleta();
        Entrega entrega2 = criarEntregaCompleta();

        // Assert
        assertEquals(entrega1, entrega2);
        assertEquals(entrega1.hashCode(), entrega2.hashCode());
    }

    @Test
    @DisplayName("Deve implementar equals corretamente para objetos diferentes")
    void deveImplementarEqualsCorretamenteParaObjetosDiferentes() {
        // Arrange
        Entrega entrega1 = criarEntregaCompleta();
        Entrega entrega2 = criarEntregaCompleta();
        entrega2.setId(999L); // ID diferente

        // Assert
        assertNotEquals(entrega1, entrega2);
    }

    @Test
    @DisplayName("Deve retornar true ao comparar objeto consigo mesmo")
    void deveRetornarTrueAoCompararObjetoConsigoMesmo() {
        // Arrange
        Entrega entrega1 = criarEntregaCompleta();

        // Assert
        assertEquals(entrega1, entrega1);
    }

    @Test
    @DisplayName("Deve retornar false ao comparar com null")
    void deveRetornarFalseAoCompararComNull() {
        // Arrange
        Entrega entrega1 = criarEntregaCompleta();

        // Assert
        assertNotEquals(entrega1, null);
    }

    @Test
    @DisplayName("Deve retornar false ao comparar com objeto de classe diferente")
    void deveRetornarFalseAoCompararComObjetoDeClasseDiferente() {
        // Arrange
        Entrega entrega1 = criarEntregaCompleta();
        String objeto = "String";

        // Assert
        assertNotEquals(entrega1, objeto);
    }

    @Test
    @DisplayName("Deve gerar hashCode consistente")
    void deveGerarHashCodeConsistente() {
        // Arrange
        Entrega entrega1 = criarEntregaCompleta();
        int hashCode1 = entrega1.hashCode();
        int hashCode2 = entrega1.hashCode();

        // Assert
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Deve permitir todos os valores de StatusEntrega")
    void devePermitirTodosOsValoresDeStatusEntrega() {
        // Arrange & Act & Assert
        entrega.setStatus(StatusEntrega.PENDENTE);
        assertEquals(StatusEntrega.PENDENTE, entrega.getStatus());

        entrega.setStatus(StatusEntrega.EM_ANDAMENTO);
        assertEquals(StatusEntrega.EM_ANDAMENTO, entrega.getStatus());

        entrega.setStatus(StatusEntrega.ENTREGUE);
        assertEquals(StatusEntrega.ENTREGUE, entrega.getStatus());

        entrega.setStatus(StatusEntrega.CANCELADA);
        assertEquals(StatusEntrega.CANCELADA, entrega.getStatus());

        entrega.setStatus(StatusEntrega.ATRASADA);
        assertEquals(StatusEntrega.ATRASADA, entrega.getStatus());
    }

    @Test
    @DisplayName("Deve aceitar data/hora nulas")
    void deveAceitarDataHoraNulas() {
        // Act
        entrega.setDataHoraEstimada(null);
        entrega.setDataHoraRealizada(null);

        // Assert
        assertNull(entrega.getDataHoraEstimada());
        assertNull(entrega.getDataHoraRealizada());
    }

    @Test
    @DisplayName("Deve aceitar endereço vazio ou nulo")
    void deveAceitarEnderecoVazioOuNulo() {
        // Act & Assert
        entrega.setEnderecoEntrega("");
        assertEquals("", entrega.getEnderecoEntrega());

        entrega.setEnderecoEntrega(null);
        assertNull(entrega.getEnderecoEntrega());
    }

    @Test
    @DisplayName("Deve trabalhar com BigDecimal para custo com precisão decimal")
    void deveTrabalharComBigDecimalParaCustoComPrecisaoDecimal() {
        // Arrange
        BigDecimal custoComDecimais = new BigDecimal("15.99");

        // Act
        entrega.setCustoEntrega(custoComDecimais);

        // Assert
        assertEquals(custoComDecimais, entrega.getCustoEntrega());
        assertEquals(2, entrega.getCustoEntrega().scale());
    }

    // Método auxiliar para criar entrega completa para testes
    private Entrega criarEntregaCompleta() {
        return new Entrega(
            1L,
            pedido,
            entregador,
            StatusEntrega.EM_ANDAMENTO,
            "Rua das Flores, 123",
            dataHoraEstimada,
            dataHoraRealizada,
            new BigDecimal("10.50")
        );
    }
}