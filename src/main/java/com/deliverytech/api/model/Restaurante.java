// src/main/java/com/deliverytech/api/model/Restaurante.java
package com.deliverytech.api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Restaurante {

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

    // Constructors
    public Restaurante() {}

    public Restaurante(Long id, String nome, String categoria, String telefone, BigDecimal taxaEntrega, 
                      Integer tempoEntregaMinutos, Boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.telefone = telefone;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

    // Getters
    public Long getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public Integer getTempoEntregaMinutos() {
        return tempoEntregaMinutos;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public void setTempoEntregaMinutos(Integer tempoEntregaMinutos) {
        this.tempoEntregaMinutos = tempoEntregaMinutos;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Restaurante restaurante = (Restaurante) obj;
        
        return java.util.Objects.equals(this.id, restaurante.id) &&
               java.util.Objects.equals(this.nome, restaurante.nome) &&
               java.util.Objects.equals(this.telefone, restaurante.telefone) &&
               java.util.Objects.equals(this.categoria, restaurante.categoria) &&
               java.util.Objects.equals(this.taxaEntrega, restaurante.taxaEntrega) &&
               java.util.Objects.equals(this.tempoEntregaMinutos, restaurante.tempoEntregaMinutos) &&
               java.util.Objects.equals(this.ativo, restaurante.ativo) &&
               java.util.Objects.equals(this.dataCadastro, restaurante.dataCadastro);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, telefone, categoria, taxaEntrega, tempoEntregaMinutos, ativo, dataCadastro);
    }
}