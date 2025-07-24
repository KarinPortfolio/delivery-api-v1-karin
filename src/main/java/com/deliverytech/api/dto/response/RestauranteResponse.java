package com.deliverytech.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta contendo os detalhes de um restaurante")
public class RestauranteResponse {

    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Deliciosa")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Pizza")
    private String categoria;

    @Schema(description = "Número de telefone do restaurante", example = "551133334444")
    private String telefone;

    @Schema(description = "Taxa de entrega do restaurante", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "Tempo mínimo de entrega em minutos", example = "30")
    private Integer tempoEntregaMinutos;

    @Schema(description = "Indica se o restaurante está ativo", example = "true")
    private Boolean ativo;
}