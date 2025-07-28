package com.deliverytech.api.service;

import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.repository.PedidoRepository;
import com.deliverytech.api.repository.EntregaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private EntregaRepository entregaRepository;

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

        // Instanciar PedidoServiceImpl com ambos os repositórios
        pedidoService = new PedidoServiceImpl(pedidoRepository, entregaRepository);
    }

    // ...existing tests from PedidoSerivceImplTest.java...
}
