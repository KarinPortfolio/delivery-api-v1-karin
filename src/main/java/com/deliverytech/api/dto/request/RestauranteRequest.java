package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RestauranteRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "Categoria é obrigatória")
    private String categoria;

    @NotNull(message = "Taxa de entrega é obrigatória")
    @DecimalMin(value = "0.00", message = "Taxa de entrega não pode ser negativa")
    private BigDecimal taxaEntrega;

    @NotNull(message = "Tempo de entrega mínimo é obrigatório")
    @Min(value = 0, message = "Tempo de entrega mínimo não pode ser negativo")
    private Integer tempoEntregaMinutos;

    private Boolean ativo; // Opcional para cadastro, padrão true

    // Constructors
    public RestauranteRequest() {
    }

    public RestauranteRequest(String nome, String telefone, String categoria, BigDecimal taxaEntrega, Integer tempoEntregaMinutos, Boolean ativo) {
        this.nome = nome;
        this.telefone = telefone;
        this.categoria = categoria;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.ativo = ativo;
    }

    // Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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