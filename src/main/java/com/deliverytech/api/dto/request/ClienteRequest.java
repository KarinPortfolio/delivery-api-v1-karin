package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema; 
public class ClienteRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do cliente", example = "Maria", required = true)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um formato válido")
    @Schema(description = "Email do cliente", example = "maria@example.com", required = true)
    private String email;

    @Schema(description = "Indica se o cliente está ativo", example = "true", required = false)
    private Boolean ativo;

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}