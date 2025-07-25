package com.deliverytech.api.model;

import jakarta.persistence.Embeddable;
import lombok.*;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um endereço para entrega")
public class Endereco {

    @Schema(description = "Nome da rua", example = "Rua das Flores")
    private String rua;
    
    @Schema(description = "Número do endereço", example = "123")
    private String numero;
    
    @Schema(description = "Bairro", example = "Centro")
    private String bairro;
    
    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;
    
    @Schema(description = "Estado", example = "SP")
    private String estado;
    
    @Schema(description = "CEP", example = "01234-567")
    private String cep;
}