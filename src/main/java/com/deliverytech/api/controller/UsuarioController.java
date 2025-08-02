package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.UsuarioRequest;
import com.deliverytech.api.dto.response.UsuarioResponse;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Criar um novo usuário", description = "Cria um novo usuário no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                content = @Content(schema = @Schema(implementation = UsuarioResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioRequest request) {
        try {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(request.getNome());
            novoUsuario.setEmail(request.getEmail());
            novoUsuario.setSenha(request.getSenha());
            novoUsuario.setRole(request.getRole());
            
            Usuario usuarioSalvo = usuarioService.criarUsuario(novoUsuario);
            UsuarioResponse response = UsuarioResponse.fromEntity(usuarioSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DataIntegrityViolationException ex) {
            // O GlobalExceptionHandler tratará esta exceção
            throw ex;
        }
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                .map(UsuarioResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id).orElse(null);
            if (usuario == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
            }
            UsuarioResponse response = UsuarioResponse.fromEntity(usuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        try {
            Usuario usuarioAtualizado = new Usuario();
            usuarioAtualizado.setNome(request.getNome());
            usuarioAtualizado.setEmail(request.getEmail());
            usuarioAtualizado.setSenha(request.getSenha());
            usuarioAtualizado.setRole(request.getRole());
            
            Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);
            UsuarioResponse response = UsuarioResponse.fromEntity(usuario);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException ex) {
            // O GlobalExceptionHandler tratará esta exceção
            throw ex;
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("não encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
            }
            // Re-lançar outras exceções para serem tratadas pelo GlobalExceptionHandler
            throw ex;
        }
    }

    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            if (ex.getMessage().contains("não encontrado")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
            }
            throw ex;
        }
    }
}
