package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Endereco;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import jakarta.annotation.Generated;
import io.swagger.v3.oas.annotations.media.Schema; 

public class PedidoRequest {
@Generated(value = "PedidoRequest.class")
    @NotNull
    @Schema(description = "ID do cliente que está fazendo o pedido", example = "1", required = true)
    private Long clienteId;

    @NotNull
    @Schema(description = "ID do restaurante onde o pedido está sendo feito", example = "1", required = true)
    private Long restauranteId;

    @NotNull
    @Schema(description = "Endereço de entrega do pedido", example = "Rua das Flores, 123", required = true)
    private Endereco enderecoEntrega;

    @NotNull
    @Schema(description = "Lista de itens do pedido", example = "[{\"produtoId\":1,\"quantidade\":2}]", required = true)
    private List<ItemPedidoRequest> itens;

    // Construtores
    public PedidoRequest() {}

    public PedidoRequest(Long clienteId, Long restauranteId, Endereco enderecoEntrega, List<ItemPedidoRequest> itens) {
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.itens = itens;
    }

    // Getters e Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequest> itens) {
        this.itens = itens;
    }
}