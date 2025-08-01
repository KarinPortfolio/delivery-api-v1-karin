package com.deliverytech.api.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class RestauranteResponse {

    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria do João", required = true)
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "Pizzaria", required = true)
    private String categoria;

    @Schema(description = "Telefone do restaurante", example = "(11) 1234-5678", required = true)
    private String telefone;

    @Schema(description = "Taxa de entrega do restaurante", example = "5.00", required = true)
    private BigDecimal taxaEntrega;

    @Schema(description = "Tempo de entrega em minutos", example = "30", required = true)
    private Integer tempoEntregaMinutos;

    @Schema(description = "Status do restaurante", example = "true", required = true)
    private Boolean ativo;

    // Constructors
    public RestauranteResponse() {
    }

    public RestauranteResponse(Long id, String nome, String categoria, String telefone, BigDecimal taxaEntrega, Integer tempoEntregaMinutos, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.telefone = telefone;
        this.taxaEntrega = taxaEntrega;
        this.tempoEntregaMinutos = tempoEntregaMinutos;
        this.ativo = ativo;
    }

    // Getters and Setters
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public BigDecimal getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(BigDecimal taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }

    public Integer getTempoEntregaMinutos() {
        return tempoEntregaMinutos;
    }

    public void setTempoEntregaMinutos(Integer tempoEntregaMinutos) {
        this.tempoEntregaMinutos = tempoEntregaMinutos;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}