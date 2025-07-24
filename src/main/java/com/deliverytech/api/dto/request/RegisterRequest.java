package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Requisição para registro de novo usuário")
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "Novo Usuário")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um formato válido")
    @Schema(description = "Endereço de e-mail do usuário (único)", example = "novo.usuario@example.com")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha do usuário (será codificada antes de salvar)", example = "senhaSegura456")
    private String senha;

    @NotNull(message = "Role é obrigatória")
    @Schema(description = "Papel (role) do usuário no sistema", example = "CLIENTE", implementation = Role.class)
    private Role role;

    @Schema(description = "ID do restaurante associado (opcional, para usuários de restaurante)", example = "1")
    private Long restauranteId;
}