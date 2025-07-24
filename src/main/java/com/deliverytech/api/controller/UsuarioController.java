package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.UsuarioRequest;
import com.deliverytech.api.dto.response.UsuarioResponse;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Operações de gerenciamento de usuários (clientes, administradores, etc.)") // Tag para agrupar no Swagger UI
@SecurityRequirement(name = "bearerAuth") 
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(
        summary = "Cria um novo usuário",
        description = "Registra um novo usuário no sistema com as informações fornecidas.",
        requestBody = @RequestBody(
            description = "Detalhes do usuário a ser criado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Usuário Cliente",
                    value = "{\"nome\": \"Novo Cliente\", \"email\": \"novo.cliente@example.com\", \"senha\": \"senhaSegura123\", \"role\": \"CLIENTE\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: email duplicado, validação falhou)",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Email já cadastrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @org.springframework.web.bind.annotation.RequestBody UsuarioRequest request) {
        Usuario novoUsuario = Usuario.builder()
                                .nome(request.getNome())
                                .email(request.getEmail())
                                .senha(request.getSenha()) 
                                .role(request.getRole())
                                .build();

        Usuario usuarioSalvo = usuarioService.criarUsuario(novoUsuario);

        UsuarioResponse response = UsuarioResponse.fromEntity(usuarioSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Lista todos os usuários",
        description = "Retorna uma lista de todos os usuários cadastrados no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                                                .map(UsuarioResponse::fromEntity)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "Busca um usuário pelo ID",
        description = "Retorna os detalhes de um usuário específico com base no seu ID.",
        parameters = @Parameter(name = "id", description = "ID do usuário a ser buscado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado com ID: 1\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarUsuarioPorId(id)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado com ID: " + id));
        UsuarioResponse response = UsuarioResponse.fromEntity(usuario);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Atualiza um usuário existente",
        description = "Atualiza os dados de um usuário com base no ID fornecido. A senha será codificada se for alterada.",
        parameters = @Parameter(name = "id", description = "ID do usuário a ser atualizado", required = true, example = "1"),
        requestBody = @RequestBody(
            description = "Novos detalhes do usuário",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UsuarioRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Atualização de Usuário",
                    value = "{\"nome\": \"Carlos Atualizado\", \"email\": \"carlos.atualizado@example.com\", \"role\": \"ADMIN\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @Valid @org.springframework.web.bind.annotation.RequestBody UsuarioRequest request) {
        Usuario usuarioParaAtualizar = Usuario.builder()
                                        .id(id) 
                                        .nome(request.getNome())
                                        .email(request.getEmail())
                                        .senha(request.getSenha())
                                        .role(request.getRole())
                                        .build();

        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioParaAtualizar);

        UsuarioResponse response = UsuarioResponse.fromEntity(usuarioAtualizado);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Deleta um usuário",
        description = "Remove um usuário do sistema com base no ID fornecido.",
        parameters = @Parameter(name = "id", description = "ID do usuário a ser deletado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso (No Content)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}