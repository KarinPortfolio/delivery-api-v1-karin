package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.EntregadorRequest;
import com.deliverytech.api.dto.response.EntregadorResponse;
import com.deliverytech.api.model.Entregador;
import com.deliverytech.api.repository.EntregadorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Tag(name = "Entregadores", description = "Gerenciamento de entregadores")
@RestController
@RequestMapping("/api/v1/entregadores")
public class EntregadorController {
    @Autowired
    private EntregadorRepository entregadorRepository;

    @Operation(summary = "Listar todos os entregadores", description = "Retorna uma lista de todos os entregadores cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de entregadores retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<EntregadorResponse>> getAllEntregadores() {
        List<Entregador> entregadores = entregadorRepository.findAll();
        List<EntregadorResponse> response = entregadores.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar entregador por ID", description = "Retorna um entregador específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntregadorResponse> getEntregadorById(@PathVariable Long id) {
        Optional<Entregador> entregador = entregadorRepository.findById(id);
        return entregador.map(e -> ResponseEntity.ok(convertToResponse(e)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado"));
    }

    @Operation(summary = "Criar um novo entregador", description = "Cria um novo entregador no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entregador criado com sucesso",
                content = @Content(schema = @Schema(implementation = EntregadorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrado")
    })
    @PostMapping
    public ResponseEntity<EntregadorResponse> createEntregador(@Valid @RequestBody EntregadorRequest entregadorRequest) {
        try {
            Entregador entregador = convertToEntity(entregadorRequest);
            entregador.setDataCriacao(LocalDateTime.now());
            Entregador savedEntregador = entregadorRepository.save(entregador);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedEntregador));
        } catch (DataIntegrityViolationException ex) {
            // O GlobalExceptionHandler tratará esta exceção
            throw ex;
        }
    }

    @Operation(summary = "Atualizar entregador", description = "Atualiza os dados de um entregador existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregador atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntregadorResponse> updateEntregador(@PathVariable Long id, @Valid @RequestBody EntregadorRequest entregadorRequest) {
        Optional<Entregador> entregador = entregadorRepository.findById(id);
        
        if (entregador.isPresent()) {
            try {
                Entregador existingEntregador = entregador.get();
                existingEntregador.setNome(entregadorRequest.getNome());
                existingEntregador.setEmail(entregadorRequest.getEmail());
                existingEntregador.setCpf(entregadorRequest.getCpf());
                existingEntregador.setTelefone(entregadorRequest.getTelefone());
                existingEntregador.setAtivo(entregadorRequest.getAtivo());
                
                Entregador updatedEntregador = entregadorRepository.save(existingEntregador);
                return ResponseEntity.ok(convertToResponse(updatedEntregador));
            } catch (DataIntegrityViolationException ex) {
                // O GlobalExceptionHandler tratará esta exceção
                throw ex;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado");
        }
    }

    @Operation(summary = "Deletar entregador", description = "Remove um entregador do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Entregador deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Entregador não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntregador(@PathVariable Long id) {
        if (entregadorRepository.existsById(id)) {
            entregadorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entregador não encontrado");
        }
    }

    // Métodos auxiliares para conversão
    private EntregadorResponse convertToResponse(Entregador entregador) {
        return new EntregadorResponse(
                entregador.getId(),
                entregador.getNome(),
                entregador.getEmail(),
                entregador.getCpf(),
                entregador.getTelefone(),
                entregador.getAtivo(),
                entregador.getDataCriacao()
        );
    }

    private Entregador convertToEntity(EntregadorRequest request) {
        Entregador entregador = new Entregador();
        entregador.setNome(request.getNome());
        entregador.setEmail(request.getEmail());
        entregador.setCpf(request.getCpf());
        entregador.setTelefone(request.getTelefone());
        entregador.setAtivo(request.getAtivo());
        return entregador;
    }
}
