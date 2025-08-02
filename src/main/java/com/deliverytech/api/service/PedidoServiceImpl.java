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
    @org.springframework.transaction.annotation.Transactional
    public void cancelar(Long id) {
        // Verificar se o pedido existe
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            throw new com.deliverytech.api.exception.EntityNotFoundException("Pedido não encontrado para cancelamento", "PEDIDO_NOT_FOUND");
        }
        
        Pedido pedido = pedidoOpt.get();
        
        // Verificar se o pedido pode ser cancelado baseado no status
        if (pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new com.deliverytech.api.exception.ConflictException(
                "Não é possível cancelar um pedido que já foi entregue", 
                "status", 
                pedido.getStatus().toString()
            );
        }
        
        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new com.deliverytech.api.exception.ConflictException(
                "Pedido já está cancelado", 
                "status", 
                pedido.getStatus().toString()
            );
        }
        
        try {
            // Ao invés de deletar, alterar o status para CANCELADO
            pedido.setStatus(StatusPedido.CANCELADO);
            pedidoRepository.save(pedido);
            
            // Verificar se há entregas associadas e cancelá-las também
            List<com.deliverytech.api.model.Entrega> entregas = entregaRepository.findByPedido_Id(id);
            for (com.deliverytech.api.model.Entrega entrega : entregas) {
                entrega.setStatus(com.deliverytech.api.model.StatusEntrega.CANCELADA);
                entregaRepository.save(entrega);
            }
            
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            throw new com.deliverytech.api.exception.ConflictException(
                "Não é possível cancelar este pedido devido a dependências no sistema", 
                "pedido", 
                id.toString()
            );
        }
    }
}