package com.deliverytech.api.dto.response;

import java.math.BigDecimal;

public class ProdutoResponse {

    private Long id;
    private String nome;
    private String categoria;
    private String descricao;
    private BigDecimal preco;
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