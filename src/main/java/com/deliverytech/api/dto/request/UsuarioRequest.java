package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema; 

public class UsuarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome do usuário", example = "João da Silva", required = true)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um formato válido")
    @Schema(description = "Email do usuário", example = "joao.silva@example.com", required = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    private String senha;

    @NotNull(message = "Role é obrigatória")
    @Schema(description = "Role do usuário", example = "CLIENTE", required = true)
    private Role role;
    
    // Constructors
    public UsuarioRequest() {}
    
    // Getters
    public String getNome() {
        return nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public Role getRole() {
        return role;
    }
    
    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }
}