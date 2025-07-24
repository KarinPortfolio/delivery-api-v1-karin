package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Requisição para cadastrar ou atualizar um produto")
public class ProdutoRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do produto", example = "Hamburguer Clássico")
    private String nome;

    @NotBlank(message = "Categoria é obrigatório")
    @Schema(description = "Categoria do produto", example = "Lanches")
    private String categoria;

    @NotBlank(message = "Descrição é obrigatória")
    @Schema(description = "Descrição detalhada do produto", example = "Pão, carne de 180g, queijo, alface, tomate e molho especial.")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.00", message = "Preço não pode ser negativo")
    @Schema(description = "Preço do produto", example = "29.90")
    private BigDecimal preco;

    @NotNull(message = "ID do restaurante é obrigatório")
    @Schema(description = "ID do restaurante ao qual o produto pertence", example = "1")
    private Long restauranteId;

    @Schema(description = "Indica se o produto está disponível (opcional, padrão true para cadastro)", example = "true")
    private Boolean disponivel; // Opcional para cadastro, padrão true
}