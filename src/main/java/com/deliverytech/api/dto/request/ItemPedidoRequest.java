package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.annotation.Generated;


public class ItemPedidoRequest {
@Generated(value = "ItemPedidoRequest.class")
    @NotNull
    private Long produtoId;

    @Positive
    private Integer quantidade;

    // Construtores
    public ItemPedidoRequest() {}

    public ItemPedidoRequest(Long produtoId, Integer quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    // Getters
    public Long getProdutoId() {
        return produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    // Setters
    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}