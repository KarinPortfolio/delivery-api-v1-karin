package com.deliverytech.api.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um item dentro de um pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do item do pedido", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @Schema(description = "Pedido ao qual este item pertence")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    @Schema(description = "Produto solicitado neste item")
    private Produto produto;

    @Schema(description = "Quantidade do produto solicitada", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto no momento do pedido", example = "25.90")
    private BigDecimal precoUnitario;

    // Getters manuais para resolver problemas do Lombok
    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}