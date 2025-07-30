package com.deliverytech.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.api.dto.request.AtualizarStatusPedidoRequest;
import com.deliverytech.api.dto.request.PedidoRequest;
import com.deliverytech.api.dto.response.PedidoResponse;
import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.service.ClienteService;
import com.deliverytech.api.service.PedidoService;
import com.deliverytech.api.service.ProdutoService;
import com.deliverytech.api.service.RestauranteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final RestauranteService restauranteService;
    private final ProdutoService produtoService;

    public PedidoController(PedidoService pedidoService, ClienteService clienteService, 
                           RestauranteService restauranteService, ProdutoService produtoService) {
        this.pedidoService = pedidoService;
        this.clienteService = clienteService;
        this.restauranteService = restauranteService;
        this.produtoService = produtoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        // 1. Buscar cliente
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(request.getClienteId());
        if (clienteOpt.isEmpty()) {
            throw new com.deliverytech.api.exception.EntityNotFoundException("Cliente", request.getClienteId());
        }
        
        // 2. Buscar restaurante
        Optional<Restaurante> restauranteOpt = restauranteService.buscarPorId(request.getRestauranteId());
        if (restauranteOpt.isEmpty()) {
            throw new com.deliverytech.api.exception.EntityNotFoundException("Restaurante", request.getRestauranteId());
        }
        
        // 3. Criar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(clienteOpt.get());
        pedido.setRestaurante(restauranteOpt.get());
        pedido.setEnderecoEntrega(request.getEnderecoEntrega());
        pedido.setStatus(com.deliverytech.api.model.StatusPedido.CRIADO);
        pedido.setDataPedido(java.time.LocalDateTime.now());
        
        // 4. Criar itens do pedido
        List<com.deliverytech.api.model.ItemPedido> itens = criarItensPedido(request.getItens(), pedido);
        pedido.setItens(itens);
        
        // 5. Calcular total baseado nos itens criados
        java.math.BigDecimal total = calcularTotalItens(itens);
        pedido.setTotal(total);
        
        // 6. Salvar pedido (os itens são salvos em cascata)
        Pedido pedidoCriado = pedidoService.criar(pedido);
        
        // 7. Converter para response
        PedidoResponse response = convertToResponse(pedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    private List<com.deliverytech.api.model.ItemPedido> criarItensPedido(
            List<com.deliverytech.api.dto.request.ItemPedidoRequest> itensRequest, 
            Pedido pedido) {
        
        List<com.deliverytech.api.model.ItemPedido> itens = new java.util.ArrayList<>();
        
        for (com.deliverytech.api.dto.request.ItemPedidoRequest itemRequest : itensRequest) {
            Optional<Produto> produtoOpt = produtoService.buscarPorId(itemRequest.getProdutoId());
            if (produtoOpt.isPresent()) {
                Produto produto = produtoOpt.get();
                
                com.deliverytech.api.model.ItemPedido item = new com.deliverytech.api.model.ItemPedido();
                item.setPedido(pedido);
                item.setProduto(produto);
                item.setQuantidade(itemRequest.getQuantidade());
                item.setPrecoUnitario(produto.getPreco());
                
                itens.add(item);
            }
        }
        
        return itens;
    }
    
    private java.math.BigDecimal calcularTotalItens(List<com.deliverytech.api.model.ItemPedido> itens) {
        return itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(java.math.BigDecimal.valueOf(item.getQuantidade())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<PedidoResponse> responses = pedidos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoService.buscarPorId(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PedidoResponse response = convertToResponse(pedidoOpt.get());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
        List<PedidoResponse> responses = pedidos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorRestaurante(@PathVariable Long restauranteId) {
        List<Pedido> pedidos = pedidoService.listarPorRestaurante(restauranteId);
        List<PedidoResponse> responses = pedidos.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusPedidoRequest request) {
        Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, request.getStatus());
        PedidoResponse response = convertToResponse(pedidoAtualizado);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatusPut(@PathVariable Long id, @Valid @RequestBody AtualizarStatusPedidoRequest request) {
        Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, request.getStatus());
        PedidoResponse response = convertToResponse(pedidoAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoResponse convertToResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setClienteId(pedido.getCliente() != null ? pedido.getCliente().getId() : null);
        response.setRestauranteId(pedido.getRestaurante() != null ? pedido.getRestaurante().getId() : null);
        response.setEnderecoEntrega(pedido.getEnderecoEntrega());
        response.setTotal(pedido.getTotal());
        response.setStatus(pedido.getStatus());
        response.setDataPedido(pedido.getDataPedido());
        
        // Mapear itens do pedido
        if (pedido.getItens() != null) {
            List<com.deliverytech.api.dto.response.ItemPedidoResponse> itensResponse = pedido.getItens().stream()
                    .map(this::convertItemToResponse)
                    .collect(Collectors.toList());
            response.setItens(itensResponse);
        } else {
            response.setItens(null);
        }
        
        return response;
    }
    
    private com.deliverytech.api.dto.response.ItemPedidoResponse convertItemToResponse(com.deliverytech.api.model.ItemPedido item) {
        return new com.deliverytech.api.dto.response.ItemPedidoResponse(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}
