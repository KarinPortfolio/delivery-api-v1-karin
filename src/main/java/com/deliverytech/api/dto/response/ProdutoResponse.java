package com.deliverytech.api.dto.response;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
public class ProdutoResponse {

    @Schema(description = "ID do produto", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String nome;

    @Schema(description = "Categoria do produto", example = "Pizzas", required = true)
    private String categoria;

    @Schema(description = "Descrição do produto", example = "Deliciosa pizza com molho de tomate e queijo", required = true)
    private String descricao;

    @Schema(description = "Preço do produto", example = "29.90", required = true)
    private BigDecimal preco;

    @Schema(description = "Disponibilidade do produto", example = "true", required = true)
    private Boolean disponivel;

    // Construtores
    public ProdutoResponse() {}

    public ProdutoResponse(Long id, String nome, String categoria, String descricao, BigDecimal preco, Boolean disponivel) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCategoria() { return categoria; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public Boolean getDisponivel() { return disponivel; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
}