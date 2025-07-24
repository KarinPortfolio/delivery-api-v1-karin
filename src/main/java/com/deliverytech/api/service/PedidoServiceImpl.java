package com.deliverytech.api.service;

import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class PedidoServiceImpl implements PedidoService { 

    private final PedidoRepository pedidoRepository;

    @Override
    public Pedido criar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> listarPorCliente(Long clienteId) {
        return List.of();
    }

    @Override
    public List<Pedido> listarPorRestaurante(Long restauranteId) {
        return List.of();
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            pedidoExistente.setStatus(novoStatus);
            return pedidoRepository.save(pedidoExistente);
        }).orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
    }

    @Override
    public void cancelar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado para cancelamento: " + id);
        }
        pedidoRepository.deleteById(id);
    }
}