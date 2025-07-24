// src/main/java/com/deliverytech/api/model/Entrega.java
package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal; // Se houver custo de entrega

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa uma entrega de pedido no sistema")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da entrega", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @Schema(description = "Pedido associado a esta entrega")
    private Pedido pedido; // Relacionamento com Pedido

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entregador_id")
    @Schema(description = "Entregador responsável por esta entrega")
    private Entregador entregador; // Relacionamento com Entregador

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual da entrega", example = "EM_ANDAMENTO", implementation = StatusEntrega.class)
    private StatusEntrega status; // Você precisará de um enum StatusEntrega

    @Schema(description = "Endereço completo de entrega", example = "Rua Principal, 456 - Bairro Novo")
    private String enderecoEntrega;

    @Schema(description = "Data e hora estimada para a entrega", example = "2025-07-23T11:00:00")
    private LocalDateTime dataHoraEstimada;

    @Schema(description = "Data e hora real da entrega", example = "2025-07-23T10:55:00")
    private LocalDateTime dataHoraRealizada;

    @Schema(description = "Custo da entrega (se aplicável)", example = "5.00")
    private BigDecimal custoEntrega;
}