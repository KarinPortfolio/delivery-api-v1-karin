package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.StatusPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.annotation.Generated;


public class AtualizarStatusPedidoRequest {

    @NotNull(message = "O status não pode ser nulo")
    private StatusPedido status;

    public AtualizarStatusPedidoRequest() {}

    @Generated(value = "AtualizarStatusPedidoRequest.class")
    public StatusPedido getStatus() { return status; }

    @Generated(value = "AtualizarStatusPedidoRequest.class")
    public void setStatus(StatusPedido status) { this.status = status; }
}
