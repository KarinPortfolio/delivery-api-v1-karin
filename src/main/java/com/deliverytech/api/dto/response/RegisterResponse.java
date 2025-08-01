// Exemplo de RegisterResponse.java
package com.deliverytech.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.deliverytech.api.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    // Construtor para compatibilidade com AuthController (sem senha)
    public RegisterResponse(Long id, String nome, String email, Role role) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
    }
    @Schema(description = "ID do usuário", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do usuário", example = "João da Silva", required = true)
    private String nome;

    @Schema(description = "Email do usuário", example = "joao.silva@example.com", required = true)
    private String email;

    @Schema(description = "Senha do usuário", example = "senha123", required = true)
    private String senha;

    @Schema(description = "Papel do usuário", example = "CLIENTE", required = true)
    private Role role;
}