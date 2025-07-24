package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Requisição para autenticação de usuário (login)")
public class LoginRequest {

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "Email deve ser um formato válido")
    @Schema(description = "Endereço de e-mail do usuário", example = "usuario@example.com")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Schema(description = "Senha do usuário", example = "senha123")
    private String senha;
}