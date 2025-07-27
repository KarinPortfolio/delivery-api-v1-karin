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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.api.dto.request.AtualizarStatusPedidoRequest;
import com.deliverytech.api.dto.request.PedidoRequest;
import com.deliverytech.api.dto.response.PedidoResponse;
import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.service.PedidoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        // Implementação básica sem validações complexas por enquanto
        Pedido pedido = new Pedido();
        // Configuração básica do pedido seria feita aqui
        
        Pedido pedidoCriado = pedidoService.criar(pedido);
        PedidoResponse response = convertToResponse(pedidoCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoResponse convertToResponse(Pedido pedido) {
        // Implementação básica de conversão
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setStatus(pedido.getStatus());
        response.setTotal(pedido.getTotal());
        response.setDataPedido(pedido.getDataPedido());
        // Outros campos seriam configurados aqui
        return response;
    }
}
