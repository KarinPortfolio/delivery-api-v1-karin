package com.deliverytech.api.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados do cliente")
public class ClienteResponse {

    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do cliente", example = "João da Silva", required = true)
    private String nome;

    @Schema(description = "Email do cliente", example = "joao.silva@example.com", required = true)
    private String email;

    @Schema(description = "Status do cliente", example = "true", required = true)
    private Boolean ativo;

    // Construtor padrão
    public ClienteResponse() {}

        // Construtor com todos os parâmetros
        public ClienteResponse(Long id, String nome, String email, Boolean ativo) {
            this.id = id;
            this.nome = nome;
            this.email = email;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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