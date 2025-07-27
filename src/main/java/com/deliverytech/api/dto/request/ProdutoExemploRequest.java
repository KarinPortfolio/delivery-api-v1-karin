package com.deliverytech.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Dados para criar um novo produto")
public class ProdutoExemploRequest {

    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", 
            example = "Pizza tradicional com molho de tomate, mussarela e manjericão")
    @Size(max = 500, message = "Descrição não pode exceder 500 caracteres")
    private String descricao;

    @Schema(description = "Preço do produto em reais", example = "25.90", required = true)
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", 
            example = "PIZZA", 
            allowableValues = {"PIZZA", "BEBIDA", "SOBREMESA", "ENTRADA"})
    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
