package com.deliverytech.api.controller;

import com.deliverytech.api.model.Entrega;
import com.deliverytech.api.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Importe para HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Importe para ResponseStatusException

import java.util.Optional;

// Importações para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/entregas") // <-- Ajustado para /api/v1/entregas
@Tag(name = "Entregas", description = "Operações de gerenciamento de entregas de pedidos") // Tag para agrupar no Swagger UI
@SecurityRequirement(name = "bearerAuth") // Indica que todos os endpoints neste controller exigem JWT
public class EntregaController {

    @Autowired
    private EntregaRepository entregaRepository;

    @Operation(
        summary = "Busca uma entrega pelo ID",
        description = "Retorna os detalhes de uma entrega específica com base no seu ID.",
        parameters = @Parameter(name = "id", description = "ID da entrega a ser buscada", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Entrega encontrada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entrega.class))),
            @ApiResponse(responseCode = "404", description = "Entrega não encontrada",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Entrega não encontrada com ID: 1\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Entrega> getEntregaById(@PathVariable Long id) {
        Optional<Entrega> entrega = entregaRepository.findById(id);
        return entrega.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrega não encontrada com ID: " + id)); // Lança 404
    }
}