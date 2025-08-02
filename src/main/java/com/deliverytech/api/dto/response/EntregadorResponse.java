package com.deliverytech.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Dados de resposta de um entregador")
public class EntregadorResponse {

    @Schema(description = "ID único do entregador", example = "1")
    private Long id;

    @Schema(description = "Nome completo do entregador", example = "Maria Julia Oliveira")
    private String nome;

    @Schema(description = "Email do entregador", example = "maju@example.com")
    private String email;

    @Schema(description = "CPF do entregador", example = "987.677.321-00")
    private String cpf;

    @Schema(description = "Telefone do entregador", example = "5511998877665")
    private String telefone;

    @Schema(description = "Status ativo do entregador", example = "true")
    private Boolean ativo;

    @Schema(description = "Data de criação do cadastro", example = "2025-08-02T10:30:00")
    private LocalDateTime dataCriacao;

    // Construtores
    public EntregadorResponse() {}

    public EntregadorResponse(Long id, String nome, String email, String cpf, String telefone, Boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    // Getters e Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
