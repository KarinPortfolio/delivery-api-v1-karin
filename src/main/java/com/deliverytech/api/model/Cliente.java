// src/main/java/com/deliverytech/api/model/Cliente.java
package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um cliente no sistema de entregas")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "Ana Clara")
    private String nome;

    @Column(unique = true)
    @Schema(description = "Endereço de e-mail do cliente (único)", example = "ana.clara@example.com")
    private String email;

    @Builder.Default
    @Schema(description = "Indica se o cliente está ativo no sistema", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Schema(description = "Data e hora de cadastro do cliente", example = "2025-07-23T10:00:00")
    private LocalDateTime dataCadastro = LocalDateTime.now();
}