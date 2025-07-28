package com.deliverytech.api.controller;

import com.deliverytech.api.model.Entregador;
import com.deliverytech.api.repository.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import jakarta.annotation.Generated;

@RestController
@RequestMapping("/api/v1/entregadores")
public class EntregadorController {
@Autowired
private EntregadorRepository entregadorRepository;

    @GetMapping
    public ResponseEntity<List<Entregador>> getAllEntregadores() {
        List<Entregador> entregadores = entregadorRepository.findAll();
        return ResponseEntity.ok(entregadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entregador> getEntregadorById(@PathVariable Long id) {
        Optional<Entregador> entregador = entregadorRepository.findById(id);
        return entregador.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
    }

    @PostMapping
    public ResponseEntity<Entregador> createEntregador(@RequestBody Entregador entregador) {
        Entregador savedEntregador = entregadorRepository.save(entregador);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntregador);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entregador> updateEntregador(@PathVariable Long id, @RequestBody Entregador entregadorDetails) {
        Optional<Entregador> entregador = entregadorRepository.findById(id);
        
        if (entregador.isPresent()) {
            Entregador existingEntregador = entregador.get();
            existingEntregador.setNome(entregadorDetails.getNome());
            existingEntregador.setEmail(entregadorDetails.getEmail());
            existingEntregador.setTelefone(entregadorDetails.getTelefone());
            existingEntregador.setAtivo(entregadorDetails.getAtivo());
            
            Entregador updatedEntregador = entregadorRepository.save(existingEntregador);
            return ResponseEntity.ok(updatedEntregador);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntregador(@PathVariable Long id) {
        if (entregadorRepository.existsById(id)) {
            entregadorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado");
        }
    }
}
