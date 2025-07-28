// src/main/java/com/deliverytech/api/service/PedidoService.java (AGORA É UMA INTERFACE)
package com.deliverytech.api.service;

import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.StatusPedido;

import java.util.List;
import java.util.Optional;


public interface PedidoService { 

    Pedido criar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> listarTodos();

    List<Pedido> listarPorCliente(Long clienteId);

    List<Pedido> listarPorRestaurante(Long restauranteId);

    Pedido atualizarStatus(Long id, StatusPedido novoStatus);

    void cancelar(Long id);
}