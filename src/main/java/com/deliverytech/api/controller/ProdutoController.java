package com.deliverytech.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.api.dto.request.ProdutoRequest;
import com.deliverytech.api.dto.response.ProdutoResponse;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.service.ProdutoService;
import com.deliverytech.api.service.RestauranteService;

import jakarta.validation.Valid;
import jakarta.annotation.Generated;


@RestController
@RequestMapping("/api/v1/produtos")
public class ProdutoController {
@Generated(value = "ProdutoController.class")
    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;

    public ProdutoController(ProdutoService produtoService, RestauranteService restauranteService) {
        this.produtoService = produtoService;
        this.restauranteService = restauranteService;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest request) {
        Optional<Restaurante> restauranteOpt = restauranteService.buscarPorId(request.getRestauranteId());
        if (restauranteOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Produto produto = new Produto();
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPreco(request.getPreco());
        produto.setCategoria(request.getCategoria());
        produto.setDisponivel(Boolean.TRUE.equals(request.getDisponivel()));
        produto.setRestaurante(restauranteOpt.get());

        Produto produtoSalvo = produtoService.cadastrar(produto);
        ProdutoResponse response = convertToResponse(produtoSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar() {
        List<Produto> produtos = produtoService.listarTodos();
        List<ProdutoResponse> responses = produtos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        Optional<Produto> produtoOpt = produtoService.buscarPorId(id);
        if (produtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ProdutoResponse response = convertToResponse(produtoOpt.get());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ProdutoResponse>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<Produto> produtos = produtoService.buscarPorRestaurante(restauranteId);
        List<ProdutoResponse> responses = produtos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        Optional<Restaurante> restauranteOpt = restauranteService.buscarPorId(request.getRestauranteId());
        if (restauranteOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Produto produtoAtualizado = new Produto();
        produtoAtualizado.setNome(request.getNome());
        produtoAtualizado.setDescricao(request.getDescricao());
        produtoAtualizado.setPreco(request.getPreco());
        produtoAtualizado.setCategoria(request.getCategoria());
        produtoAtualizado.setDisponivel(Boolean.TRUE.equals(request.getDisponivel()));
        produtoAtualizado.setRestaurante(restauranteOpt.get());

        Produto produto = produtoService.atualizar(id, produtoAtualizado);
        ProdutoResponse response = convertToResponse(produto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<ProdutoResponse> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        Optional<Produto> produtoOpt = produtoService.buscarPorId(id);
        if (produtoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        produtoService.alterarDisponibilidade(id, disponivel);
        
        // Buscar o produto atualizado para retornar
        Produto produtoAtualizado = produtoService.buscarPorId(id).get();
        ProdutoResponse response = convertToResponse(produtoAtualizado);
        return ResponseEntity.ok(response);
    }

    private ProdutoResponse convertToResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getCategoria(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getDisponivel()
        );
    }
}
