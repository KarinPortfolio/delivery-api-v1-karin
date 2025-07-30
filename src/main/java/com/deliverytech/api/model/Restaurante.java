package com.deliverytech.api.model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// Lombok annotations removed; manual methods implemented due to annotation processing issues


@Entity
// Lombok annotations removed; manual methods implemented due to annotation processing issues
public class Restaurante {
    public Restaurante() {
        // No-args constructor
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurante that = (Restaurante) o;
        return java.util.Objects.equals(id, that.id)
                && java.util.Objects.equals(nome, that.nome)
                && java.util.Objects.equals(telefone, that.telefone)
                && java.util.Objects.equals(categoria, that.categoria)
                && java.util.Objects.equals(taxaEntrega, that.taxaEntrega)
                && java.util.Objects.equals(tempoEntregaMinutos, that.tempoEntregaMinutos)
                && java.util.Objects.equals(ativo, that.ativo)
                && java.util.Objects.equals(dataCadastro, that.dataCadastro);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, telefone, categoria, taxaEntrega, tempoEntregaMinutos, ativo, dataCadastro);
    }

    // (Duplicate block removed)
    // Getter and Setter for id
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }
    public void setId(long id) { this.id = id; }

    // Getter and Setter for nome
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }

    // Getter and Setter for telefone
    public String getTelefone() { return this.telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    // Getter and Setter for categoria
    public String getCategoria() { return this.categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    // Getter and Setter for taxaEntrega
    public BigDecimal getTaxaEntrega() { return this.taxaEntrega; }
    public void setTaxaEntrega(BigDecimal taxaEntrega) { this.taxaEntrega = taxaEntrega; }

    // Getter and Setter for tempoEntregaMinutos
    public Integer getTempoEntregaMinutos() { return this.tempoEntregaMinutos; }
    public void setTempoEntregaMinutos(Integer tempoEntregaMinutos) { this.tempoEntregaMinutos = tempoEntregaMinutos; }
    public void setTempoEntregaMinutos(int tempoEntregaMinutos) { this.tempoEntregaMinutos = tempoEntregaMinutos; }

    // Getter and Setter for ativo
    public Boolean getAtivo() { return this.ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    // Getter and Setter for dataCadastro
    public LocalDateTime getDataCadastro() { return this.dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String telefone;
    private String categoria;
    private BigDecimal taxaEntrega;
    private Integer tempoEntregaMinutos;
    private Boolean ativo = true;
    private LocalDateTime dataCadastro = LocalDateTime.now();


    // Constructor matching test signature (Long, String, String, String, BigDecimal, Integer, Boolean, LocalDateTime)
    public Restaurante(Long id, String nome, String telefone, String categoria, BigDecimal taxaEntrega, Integer tempoEntregaMinutos, Boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.categoria = categoria;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

    // Constructor for primitive types (long, String, String, String, BigDecimal, int, boolean, LocalDateTime)
    public Restaurante(long id, String nome, String telefone, String categoria, BigDecimal taxaEntrega, int tempoEntregaMinutos, boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.categoria = categoria;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

}