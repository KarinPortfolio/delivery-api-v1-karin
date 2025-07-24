package com.deliverytech.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta contendo os detalhes de um cliente")
public class ClienteResponse {

    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "Ana Clara")
    private String nome;

    @Schema(description = "Endereço de e-mail do cliente", example = "ana.clara@example.com")
    private String email;

    @Schema(description = "Indica se o cliente está ativo no sistema", example = "true")
    private Boolean ativo;
}