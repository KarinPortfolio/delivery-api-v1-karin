package com.deliverytech.api.service;

import com.deliverytech.api.model.Entrega;
import java.util.List;
import java.util.Optional;

public interface EntregaService {
    
    Optional<Entrega> buscarPorId(Long id);
    
    List<Entrega> listarTodas();
    
    List<Entrega> listarPorPedido(Long pedidoId);
    
    List<Entrega> listarPorEntregador(Long entregadorId);
    
    Entrega criar(Entrega entrega);
    
    Entrega atualizar(Entrega entrega);
    
    void deletar(Long id);
}
