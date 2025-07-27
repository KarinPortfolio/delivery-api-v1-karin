package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.UsuarioRequest;
import com.deliverytech.api.dto.response.UsuarioResponse;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioRequest request) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(request.getNome());
        novoUsuario.setEmail(request.getEmail());
        novoUsuario.setSenha(request.getSenha());
        novoUsuario.setRole(request.getRole());
        
        Usuario usuarioSalvo = usuarioService.criarUsuario(novoUsuario);
        UsuarioResponse response = UsuarioResponse.fromEntity(usuarioSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodosUsuarios() {
        List<Usuario> usuarios = usuarioService.listarTodosUsuarios();
        List<UsuarioResponse> responses = usuarios.stream()
                .map(UsuarioResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id).orElse(null);
            if (usuario == null) {
                return ResponseEntity.notFound().build();
            }
            UsuarioResponse response = UsuarioResponse.fromEntity(usuario);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

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
        } catch (ResponseStatusException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
