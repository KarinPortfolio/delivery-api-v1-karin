package com.deliverytech.api.model;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status atual do pedido no fluxo de entrega")
public enum StatusPedido {
    @Schema(description = "Pedido recém criado")
    CRIADO,
    
    @Schema(description = "Pedido confirmado pelo restaurante")
    CONFIRMADO,
    
    @Schema(description = "Pedido em preparação")
    EM_PREPARACAO,
    
    @Schema(description = "Pedido enviado para entrega")
    ENVIADO,
    
    @Schema(description = "Pedido entregue ao cliente")
    ENTREGUE,
    
    @Schema(description = "Pedido cancelado")
    CANCELADO
}