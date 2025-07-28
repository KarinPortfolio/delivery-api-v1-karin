package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Endereco;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import jakarta.annotation.Generated;


public class PedidoRequest {
@Generated(value = "PedidoRequest.class")
    @NotNull
    private Long clienteId;

    @NotNull
    private Long restauranteId;

    @NotNull
    private Endereco enderecoEntrega;

    @NotNull
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