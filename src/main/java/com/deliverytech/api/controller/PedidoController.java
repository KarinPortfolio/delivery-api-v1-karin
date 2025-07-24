package com.deliverytech.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.deliverytech.api.dto.request.PedidoRequest;
import com.deliverytech.api.dto.request.AtualizarStatusPedidoRequest;
import com.deliverytech.api.dto.response.ItemPedidoResponse;
import com.deliverytech.api.dto.response.PedidoResponse;
import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.model.ItemPedido;
import com.deliverytech.api.model.Pedido;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.service.ClienteService;
import com.deliverytech.api.service.PedidoService;
import com.deliverytech.api.service.ProdutoService;
import com.deliverytech.api.service.RestauranteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos de entrega")
@SecurityRequirement(name = "bearerAuth")
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final RestauranteService restauranteService;
    private final ProdutoService produtoService;

    @Operation(
            summary = "Cria um novo pedido",
            description = "Registra um novo pedido no sistema, associando-o a um cliente e restaurante, e calculando o valor total. Detalhes do pedido a ser criado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PedidoRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Pedido",
                                    value = "{\"clienteId\": 1, \"restauranteId\": 1, \"enderecoEntrega\": \"Rua da Paz, 123 - Centro\", \"itens\": [{\"produtoId\": 1, \"quantidade\": 2}, {\"produtoId\": 2, \"quantidade\": 1}]}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Erro de validação\"}"))),
                    @ApiResponse(responseCode = "404", description = "Cliente ou Restaurante ou Produto não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Cliente não encontrado\"}"))),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoRequest request) {
        Cliente cliente = clienteService.buscarPorId(request.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
        Restaurante restaurante = restauranteService.buscarPorId(request.getRestauranteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"));

        List<ItemPedido> itens = request.getItens().stream().map(item -> {
            Produto produto = produtoService.buscarPorId(item.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + item.getProdutoId()));
            return ItemPedido.builder()
                    .produto(produto)
                    .quantidade(item.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .build();
        }).collect(Collectors.toList());

        BigDecimal total = itens.stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .restaurante(restaurante)
                .status(StatusPedido.CRIADO)
                .total(total)
                .enderecoEntrega(request.getEnderecoEntrega())
                .itens(itens)
                .build();

        Pedido salvo = pedidoService.criar(pedido);
        List<ItemPedidoResponse> itensResp = salvo.getItens().stream()
                .map(i -> new ItemPedidoResponse(i.getProduto().getId(), i.getProduto().getNome(),
                                i.getQuantidade(), i.getPrecoUnitario()))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoResponse(
                salvo.getId(),
                cliente.getId(),
                restaurante.getId(),
                salvo.getEnderecoEntrega(),
                salvo.getTotal(),
                salvo.getStatus(),
                salvo.getDataPedido(),
                itensResp));
    }

    @Operation(
            summary = "Busca um pedido pelo ID",
            description = "Retorna os detalhes completos de um pedido específico.",
            parameters = @Parameter(name = "id", description = "ID do pedido a ser buscado", required = true, example = "1"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Pedido não encontrado com ID: 1\"}"))),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado com ID: " + id));

        List<ItemPedidoResponse> itensResp = pedido.getItens().stream()
                .map(i -> new ItemPedidoResponse(i.getProduto().getId(), i.getProduto().getNome(),
                                i.getQuantidade(), i.getPrecoUnitario()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getRestaurante().getId(),
                pedido.getEnderecoEntrega(),
                pedido.getTotal(),
                pedido.getStatus(),
                pedido.getDataPedido(),
                itensResp));
    }

    @Operation(
            summary = "Atualiza o status de um pedido",
            description = "Permite alterar o status de um pedido existente (ex: CRIADO, EM_PREPARACAO, EM_TRANSITO, ENTREGUE, CANCELADO).",
            parameters = @Parameter(name = "id", description = "ID do pedido a ser atualizado", required = true, example = "1"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( // <-- CORREÇÃO AQUI
                    description = "Novo status do pedido",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AtualizarStatusPedidoRequest.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de Atualização de Status",
                                    value = "{\"status\": \"EM_PREPARACAO\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status do pedido atualizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Status inválido ou transição não permitida",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Status inválido\"}"))),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Pedido não encontrado com ID: 1\"}"))),
                    @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Não autorizado", content = @Content)
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusPedidoRequest request) {

        Pedido pedidoAtualizado = pedidoService.atualizarStatus(id, request.getStatus());

        List<ItemPedidoResponse> itensResp = pedidoAtualizado.getItens().stream()
                .map(i -> new ItemPedidoResponse(i.getProduto().getId(), i.getProduto().getNome(),
                                i.getQuantidade(), i.getPrecoUnitario()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new PedidoResponse(
                pedidoAtualizado.getId(),
                pedidoAtualizado.getCliente().getId(),
                pedidoAtualizado.getRestaurante().getId(),
                pedidoAtualizado.getEnderecoEntrega(),
                pedidoAtualizado.getTotal(),
                pedidoAtualizado.getStatus(),
                pedidoAtualizado.getDataPedido(),
                itensResp));
    }
}