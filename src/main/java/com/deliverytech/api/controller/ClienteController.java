package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.ClienteRequest;
import com.deliverytech.api.dto.response.ClienteResponse;
import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; 

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

// Importações para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody; 
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/clientes") 
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operações de gerenciamento de clientes") 
@SecurityRequirement(name = "bearerAuth") // Indica que todos os endpoints neste controller exigem JWT
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(
        summary = "Cadastra um novo cliente",
        description = "Registra um novo cliente no sistema com os dados fornecidos.",
        requestBody = @RequestBody(
            description = "Detalhes do cliente a ser cadastrado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Cliente",
                    value = "{\"nome\": \"Novo Cliente\", \"email\": \"novo.cliente@example.com\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso", // Alterado para 201 Created
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: email duplicado, validação falhou)",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Email já cadastrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@Valid @org.springframework.web.bind.annotation.RequestBody ClienteRequest request) { 
        
        Cliente cliente = Cliente.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .ativo(true)
                .build();
        Cliente salvo = clienteService.cadastrar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED) 
                .body(new ClienteResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getAtivo()));
    }

    @Operation(
        summary = "Lista clientes ativos",
        description = "Retorna uma lista de todos os clientes ativos cadastrados no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listarAtivos().stream()
                .map(c -> new ClienteResponse(c.getId(), c.getNome(), c.getEmail(), c.getAtivo()))
                .collect(Collectors.toList());
    }

    @Operation(
        summary = "Busca um cliente pelo ID",
        description = "Retorna os detalhes de um cliente específico com base no seu ID.",
        parameters = @Parameter(name = "id", description = "ID do cliente a ser buscado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Cliente não encontrado com ID: 1\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscar(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(c -> new ClienteResponse(c.getId(), c.getNome(), c.getEmail(), c.getAtivo()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado com ID: " + id)); 
    }

    @Operation(
        summary = "Atualiza um cliente existente",
        description = "Atualiza os dados de um cliente com base no ID fornecido.",
        parameters = @Parameter(name = "id", description = "ID do cliente a ser atualizado", required = true, example = "1"),
        requestBody = @RequestBody(
            description = "Novos detalhes do cliente",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Atualização de Cliente",
                    value = "{\"nome\": \"Ana Clara Atualizada\", \"email\": \"ana.clara.nova@example.com\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClienteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Cliente não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody ClienteRequest request) { 
        // A lógica de atualização está no serviço
        Cliente atualizado = Cliente.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .build();
        Cliente salvo = clienteService.atualizar(id, atualizado);
        return ResponseEntity
                .ok(new ClienteResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getAtivo()));
    }

    @Operation(
        summary = "Ativa ou desativa um cliente",
        description = "Altera o status de atividade de um cliente existente. Se o cliente estiver ativo, será desativado, e vice-versa.",
        parameters = @Parameter(name = "id", description = "ID do cliente a ter o status alterado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Status do cliente alterado com sucesso (No Content)"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Cliente não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> ativarDesativar(@PathVariable Long id) {
        clienteService.ativarDesativar(id);
        return ResponseEntity.noContent().build();
    }
}