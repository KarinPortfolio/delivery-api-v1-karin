package com.deliverytech.api.service;

import com.deliverytech.api.exception.EntityNotFoundException;
import com.deliverytech.api.model.Entrega;
import com.deliverytech.api.repository.EntregaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EntregaServiceImpl implements EntregaService {
    
    private final EntregaRepository entregaRepository;
    
    public EntregaServiceImpl(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }
    
    @Override
    public Optional<Entrega> buscarPorId(Long id) {
        return entregaRepository.findById(id);
    }
    
    @Override
    public List<Entrega> listarTodas() {
        return entregaRepository.findAll();
    }
    
    @Override
    public List<Entrega> listarPorPedido(Long pedidoId) {
        return entregaRepository.findByPedido_Id(pedidoId);
    }
    
    @Override
    public List<Entrega> listarPorEntregador(Long entregadorId) {
        return entregaRepository.findByEntregador_Id(entregadorId);
    }
    
    @Override
    @Transactional
    public Entrega criar(Entrega entrega) {
        return entregaRepository.save(entrega);
    }
    
    @Override
    @Transactional
    public Entrega atualizar(Entrega entrega) {
        if (!entregaRepository.existsById(entrega.getId())) {
            throw new EntityNotFoundException("Entrega não encontrada para atualização", "ENTREGA_NOT_FOUND");
        }
        return entregaRepository.save(entrega);
    }
    
    @Override
    @Transactional
    public void deletar(Long id) {
        if (!entregaRepository.existsById(id)) {
            throw new EntityNotFoundException("Entrega não encontrada para exclusão", "ENTREGA_NOT_FOUND");
        }
        entregaRepository.deleteById(id);
    }
}
