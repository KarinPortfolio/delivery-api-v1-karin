package com.deliverytech.api.dto.response;

import java.math.BigDecimal;

public class RestauranteResponse {

    private Long id;
    private String nome;
    private String categoria;
    private String telefone;
    private BigDecimal taxaEntrega;
    private Integer tempoEntregaMinutos;
    private Boolean ativo;

    // Constructors
    public RestauranteResponse() {
    }

    public RestauranteResponse(Long id, String nome, String categoria, String telefone, BigDecimal taxaEntrega, Integer tempoEntregaMinutos, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.telefone = telefone;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.ativo = ativo;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public Integer getTempoEntregaMinutos() {
        return tempoEntregaMinutos;
    }

    public void setTempoEntregaMinutos(Integer tempoEntregaMinutos) {
        this.tempoEntregaMinutos = tempoEntregaMinutos;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}