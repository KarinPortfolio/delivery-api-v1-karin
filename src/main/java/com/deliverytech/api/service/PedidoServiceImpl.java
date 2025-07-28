package com.deliverytech.api.service;

import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService { 

    private final PedidoRepository pedidoRepository;
    private final com.deliverytech.api.repository.EntregaRepository entregaRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             com.deliverytech.api.repository.EntregaRepository entregaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.entregaRepository = entregaRepository;
    }

    @Override
    public Pedido criar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    @Override
    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByCliente_Id(clienteId);
    }

    @Override
    public List<Pedido> listarPorRestaurante(Long restauranteId) {
        return pedidoRepository.findByRestaurante_Id(restauranteId);
    }

    @Override
    public Pedido atualizarStatus(Long id, StatusPedido novoStatus) {
        return pedidoRepository.findById(id).map(pedidoExistente -> {
            pedidoExistente.setStatus(novoStatus);
            Pedido pedidoSalvo = pedidoRepository.save(pedidoExistente);
            // Se status for ENTREGUE, criar Entrega
            if (novoStatus == com.deliverytech.api.model.StatusPedido.ENTREGUE) {
                com.deliverytech.api.model.Entrega entrega = new com.deliverytech.api.model.Entrega();
                entrega.setPedido(pedidoSalvo);
                entrega.setStatus(com.deliverytech.api.model.StatusEntrega.ENTREGUE);
                entrega.setEnderecoEntrega(pedidoSalvo.getEnderecoEntrega() != null ? pedidoSalvo.getEnderecoEntrega().toString() : null);
                entrega.setDataHoraRealizada(java.time.LocalDateTime.now());
                entregaRepository.save(entrega);
            }
            return pedidoSalvo;
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