package com.deliverytech.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.api.dto.request.RestauranteRequest;
import com.deliverytech.api.dto.response.RestauranteResponse;
import com.deliverytech.api.exception.ConflictException;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.service.RestauranteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(@Valid @RequestBody RestauranteRequest request) {
        if(restauranteService.findByNome(request.getNome())){ 
            throw new ConflictException("Já existe um restaurante cadastrado com este nome.", "nome", request.getNome());
        }

        Restaurante restaurante = new Restaurante();
        restaurante.setNome(request.getNome());
        restaurante.setTelefone(request.getTelefone());
        restaurante.setCategoria(request.getCategoria());
        restaurante.setTaxaEntrega(request.getTaxaEntrega());
        restaurante.setTempoEntregaMinutos(request.getTempoEntregaMinutos());
        restaurante.setAtivo(Boolean.TRUE.equals(request.getAtivo()));

        Restaurante restauranteSalvo = restauranteService.cadastrar(restaurante);
        RestauranteResponse response = convertToResponse(restauranteSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> listar() {
        List<Restaurante> restaurantes = restauranteService.listarTodos();
        List<RestauranteResponse> responses = restaurantes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarPorId(@PathVariable Long id) {
        Optional<Restaurante> restauranteOpt = restauranteService.buscarPorId(id);
        if (restauranteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        RestauranteResponse response = convertToResponse(restauranteOpt.get());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody RestauranteRequest request) {
        Restaurante restauranteAtualizado = new Restaurante();
        restauranteAtualizado.setNome(request.getNome());
        restauranteAtualizado.setTelefone(request.getTelefone());
        restauranteAtualizado.setCategoria(request.getCategoria());
        restauranteAtualizado.setTaxaEntrega(request.getTaxaEntrega());
        restauranteAtualizado.setTempoEntregaMinutos(request.getTempoEntregaMinutos());
        restauranteAtualizado.setAtivo(Boolean.TRUE.equals(request.getAtivo()));

        Restaurante restaurante = restauranteService.atualizar(id, restauranteAtualizado);
        RestauranteResponse response = convertToResponse(restaurante);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        restauranteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private RestauranteResponse convertToResponse(Restaurante restaurante) {
        return new RestauranteResponse(
                restaurante.getId(),
                restaurante.getNome(),
                restaurante.getCategoria(),
                restaurante.getTelefone(),
                restaurante.getTaxaEntrega(),
                restaurante.getTempoEntregaMinutos(),
                restaurante.getAtivo()
        );
    }
}
