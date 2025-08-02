package com.deliverytech.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Schema(description = "Entidade que representa um entregador")
public class Entregador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do entregador", example = "1")
    private Long id;

    @Schema(description = "Nome completo do entregador", example = "Maria Julia Oliveira")
    private String nome;

    @Column(unique = true)
    @Schema(description = "Email único do entregador", example = "maju@example.com")
    private String email;

    @Column(unique = true)
    @Schema(description = "CPF único do entregador", example = "987.677.321-00")
    private String cpf;

    @Schema(description = "Telefone do entregador", example = "5511998877665")
    private String telefone;
    
    @Schema(description = "Status ativo do entregador", example = "true")
    private Boolean ativo = true;
    
    @Schema(description = "Data de criação do cadastro")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "entregador", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Schema(description = "Lista de entregas associadas ao entregador")
    private List<Entrega> entregas;

    // Construtores
    public Entregador() {}

    public Entregador(Long id, String nome, String email, String cpf, String telefone, Boolean ativo, LocalDateTime dataCriacao, List<Entrega> entregas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.entregas = entregas;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public Boolean getAtivo() { return ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public List<Entrega> getEntregas() { return entregas; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setEntregas(List<Entrega> entregas) { this.entregas = entregas; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Entregador entregador = (Entregador) obj;
        
        return java.util.Objects.equals(this.id, entregador.id) &&
               java.util.Objects.equals(this.nome, entregador.nome) &&
               java.util.Objects.equals(this.email, entregador.email) &&
               java.util.Objects.equals(this.cpf, entregador.cpf) &&
               java.util.Objects.equals(this.telefone, entregador.telefone) &&
               java.util.Objects.equals(this.ativo, entregador.ativo) &&
               java.util.Objects.equals(this.dataCriacao, entregador.dataCriacao);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, email, cpf, telefone, ativo, dataCriacao);
    }
}