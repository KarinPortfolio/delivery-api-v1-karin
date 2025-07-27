package com.deliverytech.api.controller;

import com.deliverytech.api.model.Entrega;
import com.deliverytech.api.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/entregas")
public class EntregaController {

    @Autowired
    private EntregaRepository entregaRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Entrega> getEntregaById(@PathVariable Long id) {
        Optional<Entrega> entrega = entregaRepository.findById(id);
        return entrega.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrega não encontrada com ID: " + id));
    }
}