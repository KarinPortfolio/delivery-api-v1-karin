package com.deliverytech.api.controller;

import com.deliverytech.api.model.Entregador;
import com.deliverytech.api.repository.EntregadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Para 404/400

import java.util.List;
import java.util.Optional;

// Importações para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Cuidado para não confundir com Spring @RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/entregadores") // <-- Ajustado para /api/v1/entregadores
@Tag(name = "Entregadores", description = "Operações de gerenciamento de entregadores") // Tag para agrupar no Swagger UI
@SecurityRequirement(name = "bearerAuth") // Indica que todos os endpoints neste controller exigem JWT
public class EntregadorController {

    @Autowired
    private EntregadorRepository entregadorRepository;

    @Operation(
        summary = "Lista todos os entregadores",
        description = "Retorna uma lista de todos os entregadores cadastrados no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de entregadores retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entregador.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping
    public List<Entregador> getAllEntregadores() {
        return entregadorRepository.findAll();
    }

    @Operation(
        summary = "Busca um entregador pelo ID",
        description = "Retorna os detalhes de um entregador específico com base no seu ID.",
        parameters = @Parameter(name = "id", description = "ID do entregador a ser buscado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Entregador encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entregador.class))),
            @ApiResponse(responseCode = "404", description = "Entregador não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Entregador não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Entregador> getEntregadorById(@PathVariable Long id) {
        Optional<Entregador> entregador = entregadorRepository.findById(id);
        return entregador.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado com ID: " + id);
                });
    }

    @Operation(
        summary = "Cria um novo entregador",
        description = "Registra um novo entregador no sistema com os dados fornecidos.",
        requestBody = @RequestBody(
            description = "Detalhes do entregador a ser criado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Entregador.class),
                examples = @ExampleObject(
                    name = "Exemplo de Entregador",
                    value = "{\"nome\": \"Maria Oliveira\", \"email\": \"maria.o@example.com\", \"cpf\": \"987.654.321-00\", \"telefone\": \"5511998877665\", \"ativo\": true}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Entregador criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entregador.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: email/cpf duplicado)",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Email já cadastrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<Entregador> createEntregador(@org.springframework.web.bind.annotation.RequestBody Entregador entregador) {
        // Você pode adicionar validações de negócio aqui antes de salvar, como verificar duplicidade de email/cpf
        Entregador savedEntregador = entregadorRepository.save(entregador);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntregador);
    }

    @Operation(
        summary = "Atualiza um entregador existente",
        description = "Atualiza os dados de um entregador com base no ID fornecido. Campos não informados não serão alterados se a lógica de atualização for parcial.",
        parameters = @Parameter(name = "id", description = "ID do entregador a ser atualizado", required = true, example = "1"),
        requestBody = @RequestBody(
            description = "Novos detalhes do entregador",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Entregador.class),
                examples = @ExampleObject(
                    name = "Exemplo de Atualização",
                    value = "{\"nome\": \"Maria Silva Atualizada\", \"telefone\": \"5511999999999\", \"ativo\": false}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Entregador atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Entregador.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Entregador não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Entregador não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Entregador> updateEntregador(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody Entregador entregadorDetails) {
        Optional<Entregador> optionalEntregador = entregadorRepository.findById(id);

        if (optionalEntregador.isPresent()) {
            Entregador entregador = optionalEntregador.get();
            // Atualiza apenas os campos que foram fornecidos ou que você deseja permitir atualização
            if (entregadorDetails.getNome() != null) entregador.setNome(entregadorDetails.getNome());
            if (entregadorDetails.getEmail() != null) entregador.setEmail(entregadorDetails.getEmail());
            if (entregadorDetails.getCpf() != null) entregador.setCpf(entregadorDetails.getCpf());
            if (entregadorDetails.getTelefone() != null) entregador.setTelefone(entregadorDetails.getTelefone());
            if (entregadorDetails.getAtivo() != null) entregador.setAtivo(entregadorDetails.getAtivo());

            Entregador updatedEntregador = entregadorRepository.save(entregador);
            return ResponseEntity.ok(updatedEntregador);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado com ID: " + id);
        }
    }

    @Operation(
        summary = "Deleta um entregador",
        description = "Remove um entregador do sistema com base no ID fornecido.",
        parameters = @Parameter(name = "id", description = "ID do entregador a ser deletado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Entregador deletado com sucesso (No Content)"),
            @ApiResponse(responseCode = "404", description = "Entregador não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Entregador não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntregador(@PathVariable Long id) {
        if (entregadorRepository.existsById(id)) {
            entregadorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado com ID: " + id);
        }
    }
}