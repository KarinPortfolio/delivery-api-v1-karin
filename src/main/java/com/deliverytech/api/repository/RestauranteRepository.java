package com.deliverytech.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.api.model.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {
    List<Restaurante> findByCategoria(String categoria);

    List<Restaurante> findByAtivoTrue();

    Optional<Restaurante> findByNome(String nome);
}