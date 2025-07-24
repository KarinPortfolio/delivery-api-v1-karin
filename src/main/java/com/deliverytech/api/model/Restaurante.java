// src/main/java/com/deliverytech/api/model/Restaurante.java
package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um restaurante no sistema")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;

    @Column(unique = true)
    @Schema(description = "Nome do restaurante (único)", example = "Pizzaria Deliciosa")
    private String nome;

    @Schema(description = "Número de telefone do restaurante", example = "551133334444")
    private String telefone;

    @Schema(description = "Categoria do restaurante (ex: Pizza, Japonesa, Brasileira)", example = "Pizza")
    private String categoria;

    @Schema(description = "Taxa de entrega do restaurante", example = "5.00")
    private BigDecimal taxaEntrega;

    @Schema(description = "Tempo mínimo de entrega em minutos", example = "30")
    private Integer tempoEntregaMinutos;

    @Builder.Default
    @Schema(description = "Indica se o restaurante está ativo para receber pedidos", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Schema(description = "Data e hora de cadastro do restaurante", example = "2025-07-23T10:00:00")
    private LocalDateTime dataCadastro = LocalDateTime.now();

}