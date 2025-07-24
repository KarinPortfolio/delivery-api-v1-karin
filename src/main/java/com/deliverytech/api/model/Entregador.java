package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um entregador no sistema") // Anotação Schema na entidade
public class Entregador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do entregador", example = "1")
    private Long id;

    @Schema(description = "Nome completo do entregador", example = "João Silva")
    private String nome;

    @Column(unique = true)
    @Schema(description = "Endereço de e-mail do entregador (único)", example = "joao.silva@example.com")
    private String email;

    @Column(unique = true)
    @Schema(description = "CPF do entregador (único)", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Número de telefone do entregador", example = "5511987654321")
    private String telefone;

    @Builder.Default
    @Schema(description = "Indica se o entregador está ativo no sistema", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Schema(description = "Data e hora de criação do registro do entregador", example = "2025-07-23T10:00:00")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL)
    @Schema(description = "Lista de entregas associadas a este entregador")
    private List<Entrega> entregas;
}