package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.StatusPedido;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusPedidoRequest {

    @NotNull(message = "O status não pode ser nulo")
    private StatusPedido status;

    public AtualizarStatusPedidoRequest() {}

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
}
