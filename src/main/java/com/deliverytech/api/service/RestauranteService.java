package com.deliverytech.api.service;

import com.deliverytech.api.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteService {
    Restaurante cadastrar(Restaurante restaurante);

    Optional<Restaurante> buscarPorId(Long id);

    List<Restaurante> listarTodos();

    List<Restaurante> buscarPorCategoria(String categoria);

    Restaurante atualizar(Long id, Restaurante restauranteAtualizado);

    boolean findByNome(String nome);

    void deletar(Long id);

}