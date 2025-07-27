package com.deliverytech.api.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Endereco {

    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    // Constructors
    public Endereco() {}

    public Endereco(String rua, String numero, String bairro, String cidade, String estado, String cep) {
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // Getters
    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    // Setters
    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Endereco endereco = (Endereco) obj;
        
        return java.util.Objects.equals(this.rua, endereco.rua) &&
               java.util.Objects.equals(this.numero, endereco.numero) &&
               java.util.Objects.equals(this.bairro, endereco.bairro) &&
               java.util.Objects.equals(this.cidade, endereco.cidade) &&
               java.util.Objects.equals(this.estado, endereco.estado) &&
               java.util.Objects.equals(this.cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(rua, numero, bairro, cidade, estado, cep);
    }
}