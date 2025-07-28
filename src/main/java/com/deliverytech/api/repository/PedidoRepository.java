package com.deliverytech.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.StatusPedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCliente_Id(Long clienteId);

    List<Pedido> findByRestaurante_Id(Long restauranteId);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);
}