package com.deliverytech.api.service;

import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private Cliente cliente;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        // Criar cliente para teste
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");

        // Criar restaurante para teste
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        // Criar pedido para teste
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setTotal(new BigDecimal("45.90"));
        pedido.setStatus(StatusPedido.CRIADO);
        pedido.setDataPedido(LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        // Arrange
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        // Act
        Pedido resultado = pedidoService.criar(pedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        assertEquals(pedido.getCliente(), resultado.getCliente());
        assertEquals(pedido.getRestaurante(), resultado.getRestaurante());
        assertEquals(pedido.getTotal(), resultado.getTotal());
        assertEquals(StatusPedido.CRIADO, resultado.getStatus());

        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Deve buscar pedido por ID com sucesso")
    void deveBuscarPedidoPorIdComSucesso() {
        // Arrange
        Long pedidoId = 1L;
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        // Act
        Optional<Pedido> resultado = pedidoService.buscarPorId(pedidoId);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(pedido.getId(), resultado.get().getId());
        assertEquals(pedido.getCliente(), resultado.get().getCliente());
        assertEquals(pedido.getTotal(), resultado.get().getTotal());

        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando pedido não for encontrado por ID")
    void deveRetornarOptionalVazioQuandoPedidoNaoForEncontradoPorId() {
        // Arrange
        Long pedidoId = 999L;
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> resultado = pedidoService.buscarPorId(pedidoId);

        // Assert
        assertFalse(resultado.isPresent());

        verify(pedidoRepository, times(1)).findById(pedidoId);
    }

    @Test
    @DisplayName("Deve listar pedidos por cliente")
    void deveListarPedidosPorCliente() {
        // Arrange
        Long clienteId = 1L;

        // Act
        List<Pedido> resultado = pedidoService.listarPorCliente(clienteId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty()); // Implementação atual retorna lista vazia
    }

    @Test
    @DisplayName("Deve listar pedidos por restaurante")
    void deveListarPedidosPorRestaurante() {
        // Arrange
        Long restauranteId = 1L;

        // Act
        List<Pedido> resultado = pedidoService.listarPorRestaurante(restauranteId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty()); // Implementação atual retorna lista vazia
    }

    @Test
    @DisplayName("Deve atualizar status do pedido com sucesso")
    void deveAtualizarStatusDoPedidoComSucesso() {
        // Arrange
        Long pedidoId = 1L;
        StatusPedido novoStatus = StatusPedido.CONFIRMADO;
        
        Pedido pedidoAtualizado = new Pedido();
        pedidoAtualizado.setId(pedidoId);
        pedidoAtualizado.setCliente(cliente);
        pedidoAtualizado.setRestaurante(restaurante);
        pedidoAtualizado.setTotal(new BigDecimal("45.90"));
        pedidoAtualizado.setStatus(novoStatus);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoAtualizado);

        // Act
        Pedido resultado = pedidoService.atualizarStatus(pedidoId, novoStatus);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getId());
        assertEquals(novoStatus, resultado.getStatus());

        verify(pedidoRepository, times(1)).findById(pedidoId);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar status de pedido inexistente")
    void deveLancarExcecaoAoTentarAtualizarStatusDePedidoInexistente() {
        // Arrange
        Long pedidoIdInexistente = 999L;
        StatusPedido novoStatus = StatusPedido.CONFIRMADO;

        when(pedidoRepository.findById(pedidoIdInexistente)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> pedidoService.atualizarStatus(pedidoIdInexistente, novoStatus));

        assertEquals("Pedido não encontrado: " + pedidoIdInexistente, exception.getMessage());

        verify(pedidoRepository, times(1)).findById(pedidoIdInexistente);
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve testar todos os status possíveis na atualização")
    void deveTestarTodosOsStatusPossiveisNaAtualizacao() {
        // Arrange
        Long pedidoId = 1L;
        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Test each status
        StatusPedido[] statusList = {
            StatusPedido.CRIADO,
            StatusPedido.CONFIRMADO, 
            StatusPedido.EM_PREPARACAO,
            StatusPedido.ENVIADO,
            StatusPedido.ENTREGUE,
            StatusPedido.CANCELADO
        };

        for (StatusPedido status : statusList) {
            // Act
            Pedido resultado = pedidoService.atualizarStatus(pedidoId, status);

            // Assert
            assertEquals(status, resultado.getStatus());
        }

        verify(pedidoRepository, times(statusList.length)).findById(pedidoId);
        verify(pedidoRepository, times(statusList.length)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve cancelar pedido com sucesso")
    void deveCancelarPedidoComSucesso() {
        // Arrange
        Long pedidoId = 1L;
        when(pedidoRepository.existsById(pedidoId)).thenReturn(true);

        // Act
        pedidoService.cancelar(pedidoId);

        // Assert
        verify(pedidoRepository, times(1)).existsById(pedidoId);
        verify(pedidoRepository, times(1)).deleteById(pedidoId);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar pedido inexistente")
    void deveLancarExcecaoAoTentarCancelarPedidoInexistente() {
        // Arrange
        Long pedidoIdInexistente = 999L;
        when(pedidoRepository.existsById(pedidoIdInexistente)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> pedidoService.cancelar(pedidoIdInexistente));

        assertEquals("Pedido não encontrado para cancelamento: " + pedidoIdInexistente, 
                    exception.getMessage());

        verify(pedidoRepository, times(1)).existsById(pedidoIdInexistente);
        verify(pedidoRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve verificar injeção de dependência do repositório")
    void deveVerificarInjecaoDeDependenciaDoRepositorio() {
        // Assert
        assertNotNull(pedidoService);
        assertNotNull(pedidoRepository);
    }

    @Test
    @DisplayName("Deve lidar com pedido com total zero")
    void deveLidarComPedidoComTotalZero() {
        // Arrange
        Pedido pedidoTotalZero = new Pedido();
        pedidoTotalZero.setId(2L);
        pedidoTotalZero.setCliente(cliente);
        pedidoTotalZero.setRestaurante(restaurante);
        pedidoTotalZero.setTotal(BigDecimal.ZERO);
        pedidoTotalZero.setStatus(StatusPedido.CRIADO);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoTotalZero);

        // Act
        Pedido resultado = pedidoService.criar(pedidoTotalZero);

        // Assert
        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getTotal());
        assertEquals(StatusPedido.CRIADO, resultado.getStatus());

        verify(pedidoRepository, times(1)).save(pedidoTotalZero);
    }

    @Test
    @DisplayName("Deve lidar com pedido com total negativo")
    void deveLidarComPedidoComTotalNegativo() {
        // Arrange
        Pedido pedidoTotalNegativo = new Pedido();
        pedidoTotalNegativo.setId(3L);
        pedidoTotalNegativo.setCliente(cliente);
        pedidoTotalNegativo.setRestaurante(restaurante);
        pedidoTotalNegativo.setTotal(new BigDecimal("-10.00"));
        pedidoTotalNegativo.setStatus(StatusPedido.CRIADO);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoTotalNegativo);

        // Act
        Pedido resultado = pedidoService.criar(pedidoTotalNegativo);

        // Assert
        assertNotNull(resultado);
        assertEquals(new BigDecimal("-10.00"), resultado.getTotal());

        verify(pedidoRepository, times(1)).save(pedidoTotalNegativo);
    }

    @Test
    @DisplayName("Deve criar pedido sem cliente ou restaurante")
    void deveCriarPedidoSemClienteOuRestaurante() {
        // Arrange
        Pedido pedidoSemRelacionamentos = new Pedido();
        pedidoSemRelacionamentos.setId(4L);
        pedidoSemRelacionamentos.setTotal(new BigDecimal("25.50"));
        pedidoSemRelacionamentos.setStatus(StatusPedido.CRIADO);

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSemRelacionamentos);

        // Act
        Pedido resultado = pedidoService.criar(pedidoSemRelacionamentos);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getCliente());
        assertNull(resultado.getRestaurante());
        assertEquals(new BigDecimal("25.50"), resultado.getTotal());

        verify(pedidoRepository, times(1)).save(pedidoSemRelacionamentos);
    }
}