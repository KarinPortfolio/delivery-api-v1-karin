package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Endereco;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    @NotNull
    private Long clienteId;

    @NotNull
    private Long restauranteId;

    @NotNull
    private Endereco enderecoEntrega;

    @NotNull
    private List<ItemPedidoRequest> itens;

    // Getters manuais para resolver problemas do Lombok
    public Long getClienteId() {
        return clienteId;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public List<ItemPedidoRequest> getItens() {
        return itens;
    }
}