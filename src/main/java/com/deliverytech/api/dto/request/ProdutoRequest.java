package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProdutoRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Categoria é obrigatório")
    private String categoria;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.00", message = "Preço não pode ser negativo")
    private BigDecimal preco;

    @NotNull(message = "ID do restaurante é obrigatório")
    private Long restauranteId;

    private Boolean disponivel; // Opcional para cadastro, padrão true

    // Construtores
    public ProdutoRequest() {}

    public ProdutoRequest(String nome, String categoria, String descricao, BigDecimal preco, Long restauranteId, Boolean disponivel) {
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.preco = preco;
        this.restauranteId = restauranteId;
        this.disponivel = disponivel;
    }

    // Getters
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Long getRestauranteId() { return restauranteId; }
    public Boolean getDisponivel() { return disponivel; }

    // Setters
    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setRestauranteId(Long restauranteId) { this.restauranteId = restauranteId; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
}