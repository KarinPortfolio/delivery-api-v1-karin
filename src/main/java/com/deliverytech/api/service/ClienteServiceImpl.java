package com.deliverytech.api.service;

import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.repository.ClienteRepository; 

import org.springframework.stereotype.Service; 

import java.util.List;
import java.util.Optional;

@Service 
public class ClienteServiceImpl implements ClienteService { 

    private final ClienteRepository clienteRepository;
    
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente cadastrar(Cliente cliente) {
        // Verificar se já existe um cliente com o mesmo email
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este email: " + cliente.getEmail());
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        
        return clienteRepository.findById(id);
    }

    @Override
    public List<Cliente> listarAtivos() {
        
        return clienteRepository.findByAtivoTrue(); 
    }

    @Override
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        
        return clienteRepository.findById(id).map(clienteExistente -> {
            clienteExistente.setNome(clienteAtualizado.getNome());
            clienteExistente.setEmail(clienteAtualizado.getEmail());
            
            return clienteRepository.save(clienteExistente);
        }).orElseThrow(() -> new RuntimeException("Cliente não encontrado para atualização: " + id));
    }

    @Override
    public void ativarDesativar(Long id) {
        
        clienteRepository.findById(id).ifPresent(cliente -> {
            cliente.setAtivo(!cliente.getAtivo());
            clienteRepository.save(cliente);
        });
    }
}