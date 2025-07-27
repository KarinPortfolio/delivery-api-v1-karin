package com.deliverytech.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private Boolean ativo = true;

    private LocalDateTime dataCadastro = LocalDateTime.now();

    // Constructors
    public Cliente() {}

    public Cliente(Long id, String nome, String email, Boolean ativo, LocalDateTime dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
    }

    // Getters manuais para resolver problemas do Lombok
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    // Setters manuais para resolver problemas do Lombok
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    // Builder manual para resolver problemas do Lombok
    public static ClienteBuilder builder() {
        return new ClienteBuilder();
    }

    public static class ClienteBuilder {
        private Long id;
        private String nome;
        private String email;
        private Boolean ativo = true;
        private LocalDateTime dataCadastro = LocalDateTime.now();

        public ClienteBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ClienteBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ClienteBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ClienteBuilder ativo(Boolean ativo) {
            this.ativo = ativo;
            return this;
        }

        public ClienteBuilder dataCadastro(LocalDateTime dataCadastro) {
            this.dataCadastro = dataCadastro;
            return this;
        }

        public Cliente build() {
            Cliente cliente = new Cliente();
            cliente.id = this.id;
            cliente.nome = this.nome;
            cliente.email = this.email;
            cliente.ativo = this.ativo;
            cliente.dataCadastro = this.dataCadastro;
            return cliente;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Cliente cliente = (Cliente) obj;
        
        return java.util.Objects.equals(this.id, cliente.id) &&
               java.util.Objects.equals(this.nome, cliente.nome) &&
               java.util.Objects.equals(this.email, cliente.email) &&
               java.util.Objects.equals(this.ativo, cliente.ativo) &&
               java.util.Objects.equals(this.dataCadastro, cliente.dataCadastro);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, email, ativo, dataCadastro);
    }
}