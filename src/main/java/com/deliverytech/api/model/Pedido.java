package com.deliverytech.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Representa um pedido no sistema")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do pedido", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @Schema(description = "Cliente que fez o pedido")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante onde o pedido foi feito")
    private Restaurante restaurante;

    @Schema(description = "Valor total do pedido", example = "89.90")
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual do pedido", example = "PENDENTE")
    private StatusPedido status;

    @Builder.Default
    @Schema(description = "Data e hora do pedido", example = "2025-07-24T14:30:00")
    private LocalDateTime dataPedido = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    @Schema(description = "Lista de itens do pedido")
    private List<ItemPedido> itens;

    @Embedded
    @Schema(description = "Endereço de entrega do pedido")
    private Endereco enderecoEntrega;

    // Getters manuais para resolver problemas do Lombok
    public List<ItemPedido> getItens() {
        return itens;
    }

    public Endereco getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }
}