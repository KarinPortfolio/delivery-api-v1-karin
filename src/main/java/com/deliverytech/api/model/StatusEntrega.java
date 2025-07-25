package com.deliverytech.api.model;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status atual da entrega")
public enum StatusEntrega {
    @Schema(description = "Entrega pendente, aguardando entregador")
    PENDENTE,
    
    @Schema(description = "Entrega em andamento")
    EM_ANDAMENTO,
    
    @Schema(description = "Entrega realizada com sucesso")
    ENTREGUE,
    
    @Schema(description = "Entrega cancelada")
    CANCELADA,
    
    @Schema(description = "Entrega atrasada")
    ATRASADA
}