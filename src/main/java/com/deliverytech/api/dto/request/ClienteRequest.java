package com.deliverytech.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Schema(description = "Requisição para cadastrar ou atualizar um cliente")
public class ClienteRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do cliente", example = "João Teste")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser um formato válido")
    @Schema(description = "Endereço de e-mail do cliente (único)", example = "joao.teste@example.com")
    private String email;

    // Getters manuais para resolver problemas do Lombok
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}