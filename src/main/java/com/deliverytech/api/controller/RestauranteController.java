package com.deliverytech.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException; 


import com.deliverytech.api.dto.request.RestauranteRequest;
import com.deliverytech.api.dto.response.RestauranteResponse;
import com.deliverytech.api.exception.ConflictException; 

import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.service.RestauranteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


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
@RequestMapping("/api/v1/restaurantes") 
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Operações de gerenciamento de restaurantes") 
@SecurityRequirement(name = "bearerAuth") 
public class RestauranteController {

    private final RestauranteService restauranteService;

    @Operation(
        summary = "Cadastra um novo restaurante",
        description = "Registra um novo restaurante no sistema com os dados fornecidos. O nome do restaurante deve ser único.",
        requestBody = @RequestBody(
            description = "Detalhes do restaurante a ser cadastrado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestauranteRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Restaurante",
                    value = "{\"nome\": \"Novo Restaurante\", \"telefone\": \"5511912345678\", \"categoria\": \"Brasileira\", \"taxaEntrega\": 8.50, \"tempoEntregaMinutos\": 40, \"ativo\": true}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Restaurante cadastrado com sucesso", // Alterado para 201 Created
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Erro de validação\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflito: Já existe um restaurante com este nome",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 409, \"error\": \"Conflict\", \"message\": \"Já existe um restaurante cadastrado com este nome.\" }"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<RestauranteResponse> cadastrar(@Valid @org.springframework.web.bind.annotation.RequestBody RestauranteRequest request) { // <-- AGORA SEMPRE QUALIFICADO
        if(restauranteService.findByNome(request.getNome())){ 
            throw new ConflictException("Já existe um restaurante cadastrado com este nome.", "nome", request.getNome());
        }

        Restaurante restaurante = Restaurante.builder()
            .nome(request.getNome())
            .telefone(request.getTelefone())
            .categoria(request.getCategoria())
            .taxaEntrega(request.getTaxaEntrega())
            .tempoEntregaMinutos(request.getTempoEntregaMinutos())
            .ativo(request.getAtivo() != null ? request.getAtivo() : true) 
            .build();
        Restaurante salvo = restauranteService.cadastrar(restaurante);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RestauranteResponse( 
            salvo.getId(),salvo.getNome(),salvo.getCategoria(),salvo.getTelefone(),
            salvo.getTaxaEntrega(),salvo.getTempoEntregaMinutos(),salvo.getAtivo()));
    }

    @Operation(
        summary = "Lista todos os restaurantes",
        description = "Retorna uma lista de todos os restaurantes cadastrados no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping
    public List<RestauranteResponse> listarTodos() {
        return restauranteService.listarTodos().stream()
                .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(),
                        r.getTelefone(),
                        r.getTaxaEntrega(), r.getTempoEntregaMinutos(), r.getAtivo()))
                .collect(Collectors.toList());
    }

    @Operation(
        summary = "Busca um restaurante pelo ID",
        description = "Retorna os detalhes de um restaurante específico com base no seu ID.",
        parameters = @Parameter(name = "id", description = "ID do restaurante a ser buscado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Restaurante não encontrado com ID: 1\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> buscarPorId(@PathVariable Long id) {
        return restauranteService.buscarPorId(id)
                .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(),
                        r.getTelefone(),
                        r.getTaxaEntrega(), r.getTempoEntregaMinutos(), r.getAtivo()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado com ID: " + id)); // Lança 404
    }

    @Operation(
        summary = "Busca restaurantes por categoria",
        description = "Retorna uma lista de restaurantes que pertencem a uma categoria específica.",
        parameters = @Parameter(name = "categoria", description = "Categoria dos restaurantes a serem buscados", required = true, example = "Pizza"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de restaurantes por categoria retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/categoria/{categoria}")
    public List<RestauranteResponse> buscarPorCategoria(@PathVariable String categoria) {
        return restauranteService.buscarPorCategoria(categoria).stream()
                .map(r -> new RestauranteResponse(r.getId(), r.getNome(), r.getCategoria(),
                        r.getTelefone(),
                        r.getTaxaEntrega(), r.getTempoEntregaMinutos(), r.getAtivo()))
                .collect(Collectors.toList());
    }

    @Operation(
        summary = "Atualiza um restaurante existente",
        description = "Atualiza os dados de um restaurante com base no ID fornecido.",
        parameters = @Parameter(name = "id", description = "ID do restaurante a ser atualizado", required = true, example = "1"),
        requestBody = @RequestBody(
            description = "Novos detalhes do restaurante",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestauranteRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Atualização de Restaurante",
                    value = "{\"nome\": \"Pizzaria Deliciosa Atualizada\", \"telefone\": \"551177778888\", \"taxaEntrega\": 6.00, \"ativo\": false}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Restaurante não encontrado com ID: 1\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflito: Já existe um restaurante com este nome",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 409, \"error\": \"Conflict\", \"message\": \"Já existe um restaurante cadastrado com este nome.\" }"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> atualizar(@PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody RestauranteRequest request) {

        Restaurante atualizado = Restaurante.builder()
                .nome(request.getNome())
                .telefone(request.getTelefone())
                .categoria(request.getCategoria())
                .taxaEntrega(request.getTaxaEntrega())
                .tempoEntregaMinutos(request.getTempoEntregaMinutos())
                .ativo(request.getAtivo()) 
                .build();
        Restaurante salvo = restauranteService.atualizar(id, atualizado);
        return ResponseEntity.ok(new RestauranteResponse(salvo.getId(), salvo.getNome(), salvo.getCategoria(),
                salvo.getTelefone(), salvo.getTaxaEntrega(), salvo.getTempoEntregaMinutos(),
                salvo.getAtivo()));
    }

    @Operation(
        summary = "Deleta um restaurante",
        description = "Remove um restaurante do sistema com base no ID fornecido.",
        parameters = @Parameter(name = "id", description = "ID do restaurante a ser deletado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante deletado com sucesso (No Content)"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Restaurante não encontrado com ID: 1\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarRestaurante(@PathVariable Long id) {
        restauranteService.deletar(id); 
        return ResponseEntity.noContent().build();
    }
}