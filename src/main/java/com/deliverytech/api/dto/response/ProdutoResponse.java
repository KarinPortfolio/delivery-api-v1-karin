package com.deliverytech.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta contendo os detalhes de um produto")
public class ProdutoResponse {

    @Schema(description = "ID único do produto", example = "101")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Calabresa Grande")
    private String nome;

    @Schema(description = "Categoria do produto", example = "Pizzas")
    private String categoria;

    @Schema(description = "Descrição detalhada do produto", example = "Pizza com molho de tomate, mussarela, calabresa e cebola.")
    private String descricao;

    @Schema(description = "Preço do produto", example = "45.90")
    private BigDecimal preco;

    @Schema(description = "Indica se o produto está disponível para venda", example = "true")
    private Boolean disponivel;
}