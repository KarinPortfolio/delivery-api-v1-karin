package com.deliverytech.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.api.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByRestauranteId(Long restauranteId);

    List<Produto> findByDisponivelTrue();

    List<Produto> findByCategoria(String categoria);
}