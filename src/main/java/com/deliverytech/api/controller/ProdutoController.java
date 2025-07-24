package com.deliverytech.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus; // Importe para HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
// REMOVIDO: import org.springframework.web.bind.annotation.RequestBody; // <-- REMOVA ESTA LINHA
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException; // Importe para ResponseStatusException

import com.deliverytech.api.dto.request.ProdutoRequest;
import com.deliverytech.api.dto.response.ProdutoResponse;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.service.ProdutoService;
import com.deliverytech.api.service.RestauranteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
@RequestMapping("/api/v1/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Operações de gerenciamento de produtos de restaurantes") // Tag para agrupar no Swagger UI
@SecurityRequirement(name = "bearerAuth") // Indica que todos os endpoints neste controller exigem JWT
public class ProdutoController {

    private final ProdutoService produtoService;
    private final RestauranteService restauranteService;

    @Operation(
        summary = "Cadastra um novo produto",
        description = "Registra um novo produto para um restaurante específico.",
        requestBody = @RequestBody(
            description = "Detalhes do produto a ser cadastrado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProdutoRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Produto",
                    value = "{\"nome\": \"Refrigerante Cola\", \"categoria\": \"Bebidas\", \"descricao\": \"Lata de 350ml\", \"preco\": 7.00, \"restauranteId\": 1, \"disponivel\": true}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "201", description = "Produto cadastrado com sucesso", // Alterado para 201 Created
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Erro de validação\"}"))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Restaurante não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @org.springframework.web.bind.annotation.RequestBody ProdutoRequest request) { // <-- AGORA SEMPRE QUALIFICADO
        Restaurante restaurante = restauranteService.buscarPorId(request.getRestauranteId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        Produto produto = Produto.builder()
            .nome(request.getNome())
            .categoria(request.getCategoria())
            .descricao(request.getDescricao())
            .preco(request.getPreco())
            .disponivel(request.getDisponivel() != null ? request.getDisponivel() : true) // Usa o disponivel do request ou padrão true
            .restaurante(restaurante)
            .build();

        Produto salvo = produtoService.cadastrar(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProdutoResponse( // Retorna 201 Created
            salvo.getId(), salvo.getNome(), salvo.getCategoria(), salvo.getDescricao(), salvo.getPreco(),
            salvo.getDisponivel()));
    }

    @Operation(
        summary = "Lista todos os produtos",
        description = "Retorna uma lista de todos os produtos cadastrados no sistema.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping
    public List<ProdutoResponse> listarTodos() {
        return produtoService.listarTodos().stream()
                .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(), p.getDescricao(), p.getPreco(),
                        p.getDisponivel()))
                .collect(Collectors.toList());
    }

    @Operation(
        summary = "Busca um produto pelo ID",
        description = "Retorna os detalhes de um produto específico com base no seu ID.",
        parameters = @Parameter(name = "id", description = "ID do produto a ser buscado", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Produto não encontrado com ID: 1\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id)
                .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(), p.getDescricao(), p.getPreco(),
                        p.getDisponivel()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado com ID: " + id)); // Lança 404
    }

    @Operation(
        summary = "Lista produtos por restaurante",
        description = "Retorna uma lista de produtos pertencentes a um restaurante específico.",
        parameters = @Parameter(name = "restauranteId", description = "ID do restaurante cujos produtos serão listados", required = true, example = "1"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos por restaurante retornada com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Restaurante não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @GetMapping("/restaurante/{restauranteId}")
    public List<ProdutoResponse> listarPorRestaurante(@PathVariable Long restauranteId) {
        // Você pode adicionar uma verificação se o restaurante existe aqui, se preferir lançar 404
        // restauranteService.buscarPorId(restauranteId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));
        return produtoService.buscarPorRestaurante(restauranteId).stream()
            .map(p -> new ProdutoResponse(p.getId(), p.getNome(), p.getCategoria(), p.getDescricao(), p.getPreco(),
            p.getDisponivel()))
            .collect(Collectors.toList());
    }

    @Operation(
        summary = "Atualiza um produto existente",
        description = "Atualiza os dados de um produto com base no ID fornecido.",
        parameters = @Parameter(name = "id", description = "ID do produto a ser atualizado", required = true, example = "1"),
        requestBody = @RequestBody(
            description = "Novos detalhes do produto",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProdutoRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Atualização de Produto",
                    value = "{\"nome\": \"Hamburguer Duplo\", \"preco\": 35.00, \"disponivel\": false}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto ou Restaurante não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Produto não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody ProdutoRequest request) {
        // A lógica de atualização está no serviço
        Produto atualizado = Produto.builder()
            .nome(request.getNome())
            .categoria(request.getCategoria())
            .descricao(request.getDescricao())
            .preco(request.getPreco())
            .disponivel(request.getDisponivel()) // Permite atualizar o status disponivel
            .build();
        Produto salvo = produtoService.atualizar(id, atualizado);
        return ResponseEntity.ok(new ProdutoResponse(salvo.getId(), salvo.getNome(), salvo.getCategoria(),
            salvo.getDescricao(), salvo.getPreco(), salvo.getDisponivel()));
    }

    @Operation(
        summary = "Altera a disponibilidade de um produto",
        description = "Define se um produto está disponível ou indisponível para venda.",
        parameters = {
            @Parameter(name = "id", description = "ID do produto a ter a disponibilidade alterada", required = true, example = "1"),
            @Parameter(name = "disponivel", description = "Status de disponibilidade (true para disponível, false para indisponível)", required = true, example = "true")
        },
        responses = {
            @ApiResponse(responseCode = "204", description = "Disponibilidade alterada com sucesso (No Content)"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Produto não encontrado\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
        }
    )
    @PatchMapping("/{id}/disponibilidade")
    public ResponseEntity<Void> alterarDisponibilidade(@PathVariable Long id, @RequestParam boolean disponivel) {
        produtoService.alterarDisponibilidade(id, disponivel);
        return ResponseEntity.noContent().build();
    }
}