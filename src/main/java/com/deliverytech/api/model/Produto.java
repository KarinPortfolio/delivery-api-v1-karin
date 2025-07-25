package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um produto oferecido por um restaurante")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto", example = "101")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Calabresa Grande")
    private String nome;

    @Schema(description = "Categoria do produto (ex: Pizzas, Bebidas, Sobremesas)", example = "Pizzas")
    private String categoria;

    @Schema(description = "Descrição detalhada do produto", example = "Pizza com molho de tomate, mussarela, calabresa e cebola.")
    private String descricao;

    @Schema(description = "Preço do produto", example = "45.90")
    private BigDecimal preco;

    @Builder.Default
    @Schema(description = "Indica se o produto está disponível para venda", example = "true")
    private Boolean disponivel = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante ao qual o produto pertence")
    private Restaurante restaurante;

    // Getters manuais para resolver problemas do Lombok
    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }
}