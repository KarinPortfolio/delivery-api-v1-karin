package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Requisição para cadastrar ou atualizar um restaurante")
public class RestauranteRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do restaurante", example = "Churrascaria Gaúcha")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    @Schema(description = "Número de telefone do restaurante", example = "551155556666")
    private String telefone;

    @NotBlank(message = "Categoria é obrigatória")
    @Schema(description = "Categoria do restaurante", example = "Churrasco")
    private String categoria;

    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.00", message = "Taxa de entrega não pode ser negativa")
    @Schema(description = "Taxa de entrega do restaurante", example = "7.50")
    private BigDecimal taxaEntrega;

    @NotNull(message = "Tempo de entrega mínimo é obrigatório")
    @Min(value = 0, message = "Tempo de entrega mínimo não pode ser negativo")
    @Schema(description = "Tempo mínimo de entrega em minutos", example = "45")
    private Integer tempoEntregaMinutos;

    @Schema(description = "Indica se o restaurante está ativo (opcional, padrão true para cadastro)", example = "true")
    private Boolean ativo; // Opcional para cadastro, padrão true
}