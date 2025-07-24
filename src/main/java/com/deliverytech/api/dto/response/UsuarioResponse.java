package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta contendo os detalhes de um usuário")
public class UsuarioResponse {

    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Carlos Souza")
    private String nome;

    @Schema(description = "Endereço de e-mail do usuário", example = "carlos.souza@example.com")
    private String email;

    @Schema(description = "Papel (role) do usuário no sistema", example = "CLIENTE", implementation = Role.class)
    private Role role;

    @Schema(description = "Indica se a conta do usuário está ativa", example = "true")
    private Boolean ativo;

    @Schema(description = "Data e hora de criação da conta do usuário", example = "2025-07-23T10:00:00")
    private LocalDateTime dataCriacao;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao())
                .build();
    }
}