package com.deliverytech.api.service;

import java.util.List;
import java.util.Optional;

import com.deliverytech.api.model.Cliente;

public interface ClienteService {
    Cliente cadastrar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> listarAtivos();

    Cliente atualizar(Long id, Cliente clienteAtualizado);

    void ativarDesativar(Long id);
}