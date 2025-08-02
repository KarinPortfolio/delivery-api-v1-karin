package com.deliverytech.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Dados para criação de um entregador")
public class EntregadorRequest {

    @Schema(description = "Nome completo do entregador", example = "Maria Julia Oliveira", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Email do entregador", example = "maju@example.com", required = true)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;

    @Schema(description = "CPF do entregador", example = "987.677.321-00", required = true)
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF deve seguir o formato XXX.XXX.XXX-XX")
    private String cpf;

    @Schema(description = "Telefone do entregador", example = "5511998877665", required = true)
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "^\\d{10,13}$", message = "Telefone deve ter entre 10 e 13 dígitos")
    private String telefone;

    @Schema(description = "Status ativo do entregador", example = "true", defaultValue = "true")
    private Boolean ativo = true;

    // Construtores
    public EntregadorRequest() {}

    public EntregadorRequest(String nome, String email, String cpf, String telefone, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ativo = ativo;
    }

    // Getters e Setters
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
}
