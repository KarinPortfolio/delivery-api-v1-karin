package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtualizarStatusPedidoRequest {

    @NotNull(message = "O status não pode ser nulo")
    private StatusPedido status;
}
