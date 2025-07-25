package com.deliverytech.api.model;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Papel/função do usuário no sistema")
public enum Role {
    @Schema(description = "Cliente que faz pedidos")
    CLIENTE,
    
    @Schema(description = "Administrador de restaurante")
    RESTAURANTE,
    
    @Schema(description = "Administrador do sistema")
    ADMIN,
    
    @Schema(description = "Entregador que realiza as entregas")
    ENTREGADOR
}