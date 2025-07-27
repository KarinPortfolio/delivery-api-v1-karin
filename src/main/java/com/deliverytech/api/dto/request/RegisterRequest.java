package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados necessários para registrar um novo usuário")
public class RegisterRequest {

    @Schema(description = "Nome completo do usuário", example = "João Silva", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Email único do usuário", example = "joao@exemplo.com", required = true)
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um formato válido")
    private String email;

    @Schema(description = "Senha do usuário (mínimo 6 caracteres)", example = "123456", required = true)
    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @Schema(description = "Tipo de usuário", example = "CLIENTE", required = true, 
            allowableValues = {"CLIENTE", "RESTAURANTE", "ENTREGADOR"})
    @NotNull(message = "Role é obrigatória")
    private Role role;

    private Long restauranteId;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public void setRestauranteId(Long restauranteId) {
        this.restauranteId = restauranteId;
    }
}