package com.deliverytech.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados necessários para realizar login no sistema")
public class LoginRequest {

    @Schema(description = "Email do usuário", example = "usuario@exemplo.com", required = true)
    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Email deve ser um formato válido")
    private String email;

    @Schema(description = "Senha do usuário", example = "123456", required = true)
    @NotBlank(message = "A senha não pode estar em branco")
    private String senha;

    // Constructors
    public LoginRequest() {
    }

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters
    public String getEmail() {
        return email;
    }
    
    public String getSenha() {
        return senha;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}