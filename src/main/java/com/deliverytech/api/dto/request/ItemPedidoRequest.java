package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequest {

    @NotNull
    private Long produtoId;

    @Positive
    private Integer quantidade;

    // Getters manuais para resolver problemas do Lombok
    public Long getProdutoId() {
        return produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }
}