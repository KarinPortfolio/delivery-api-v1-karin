package com.deliverytech.api.controller;

import com.deliverytech.api.dto.response.EntregaResponse;
import com.deliverytech.api.model.Entrega;
import com.deliverytech.api.service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Entregas", description = "Gerenciamento de entregas")
@RestController
@RequestMapping("/api/v1/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @Operation(summary = "Buscar entrega por ID", description = "Retorna uma entrega específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega encontrada com sucesso",
                content = @Content(schema = @Schema(implementation = EntregaResponse.class))),
        @ApiResponse(responseCode = "404", description = "Entrega não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntregaResponse> getEntregaById(@PathVariable Long id) {
        Optional<Entrega> entrega = entregaService.buscarPorId(id);
        return entrega.map(e -> ResponseEntity.ok(convertToResponse(e)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrega não encontrada com ID: " + id));
    }

    @Operation(summary = "Listar todas as entregas", description = "Retorna uma lista de todas as entregas cadastradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de entregas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<EntregaResponse>> getAllEntregas() {
        List<Entrega> entregas = entregaService.listarTodas();
        List<EntregaResponse> response = entregas.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Método auxiliar para conversão
    private EntregaResponse convertToResponse(Entrega entrega) {
        EntregaResponse response = new EntregaResponse();
        response.setId(entrega.getId());
        response.setStatus(entrega.getStatus());
        response.setEnderecoEntrega(entrega.getEnderecoEntrega());
        response.setDataHoraEstimada(entrega.getDataHoraEstimada());
        response.setDataHoraRealizada(entrega.getDataHoraRealizada());
        response.setCustoEntrega(entrega.getCustoEntrega());

        // Informações do pedido (evitando referência circular)
        if (entrega.getPedido() != null) {
            response.setPedidoId(entrega.getPedido().getId());
            response.setValorPedido(entrega.getPedido().getTotal());
            
            if (entrega.getPedido().getCliente() != null) {
                response.setNomeCliente(entrega.getPedido().getCliente().getNome());
            }
            
            if (entrega.getPedido().getRestaurante() != null) {
                response.setNomeRestaurante(entrega.getPedido().getRestaurante().getNome());
            }
        }

        // Informações do entregador (evitando referência circular)
        if (entrega.getEntregador() != null) {
            response.setEntregadorId(entrega.getEntregador().getId());
            response.setNomeEntregador(entrega.getEntregador().getNome());
        }

        return response;
    }
}