package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public class UsuarioResponse {

    @Schema(description = "ID do usuário", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do usuário", example = "João da Silva", required = true)
    private String nome;

    @Schema(description = "Email do usuário", example = "joao.silva@example.com", required = true)
    private String email;

    @Schema(description = "Papel do usuário", example = "CLIENTE", required = true)
    private Role role;

    @Schema(description = "Status do usuário", example = "true", required = true)
        private Boolean ativo;
    private LocalDateTime dataCriacao;

    // Constructors
    public UsuarioResponse() {
    }

    public UsuarioResponse(Long id, String nome, String email, Role role, Boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // Factory method
    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getAtivo(),
                usuario.getDataCriacao()
        );
    }
}