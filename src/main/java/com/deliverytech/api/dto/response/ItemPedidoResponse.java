package com.deliverytech.api.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class ItemPedidoResponse {
    @Schema(description = "ID do produto", example = "1", required = true)
    private Long produtoId;

    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String nomeProduto;

    @Schema(description = "Quantidade do produto", example = "2", required = true)
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto", example = "29.90", required = true)
        private BigDecimal precoUnitario;

    public ItemPedidoResponse() {}

    public ItemPedidoResponse(Long produtoId, String nomeProduto, Integer quantidade, BigDecimal precoUnitario) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}