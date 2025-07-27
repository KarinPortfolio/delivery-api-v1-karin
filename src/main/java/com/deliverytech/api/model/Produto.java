package com.deliverytech.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String categoria;
    private String descricao;
    private BigDecimal preco;
    private Boolean disponivel = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    // Constructors
    public Produto() {}

    public Produto(Long id, String nome, String categoria, String descricao, BigDecimal preco, Boolean disponivel, Restaurante restaurante) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.descricao = descricao;
        this.preco = preco;
        this.disponivel = disponivel;
        this.restaurante = restaurante;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Produto produto = (Produto) obj;
        
        // Compara todos os campos principais
        return java.util.Objects.equals(this.id, produto.id) &&
               java.util.Objects.equals(this.nome, produto.nome) &&
               java.util.Objects.equals(this.categoria, produto.categoria) &&
               java.util.Objects.equals(this.descricao, produto.descricao) &&
               java.util.Objects.equals(this.preco, produto.preco) &&
               java.util.Objects.equals(this.disponivel, produto.disponivel) &&
               java.util.Objects.equals(this.restaurante, produto.restaurante);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, categoria, descricao, preco, disponivel, restaurante);
    }
}