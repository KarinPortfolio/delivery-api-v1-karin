package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.model.Endereco;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    @Schema(description = "ID do pedido", example = "1", required = true)
    private Long id;

    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long clienteId;

    @Schema(description = "ID do restaurante", example = "1", required = true)
    private Long restauranteId;

    @Schema(description = "Endereço de entrega", required = true)
    private Endereco enderecoEntrega;

    @Schema(description = "Valor total do pedido", example = "100.00", required = true)
    private BigDecimal total;

    @Schema(description = "Status do pedido", example = "PENDENTE", required = true)
    private StatusPedido status;

    @Schema(description = "Data do pedido", example = "2023-03-15T10:00:00", required = true)
    private LocalDateTime dataPedido;

    @Schema(description = "Itens do pedido", required = true)
    private List<ItemPedidoResponse> itens;

    public PedidoResponse() {}

    public PedidoResponse(Long id, Long clienteId, Long restauranteId, Endereco enderecoEntrega, 
                         BigDecimal total, StatusPedido status, LocalDateTime dataPedido, 
                         List<ItemPedidoResponse> itens) {
        this.id = id;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.enderecoEntrega = enderecoEntrega;
        this.total = total;
        this.status = status;
        this.dataPedido = dataPedido;
        this.itens = itens;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getRestauranteId() { return restauranteId; }
    public void setRestauranteId(Long restauranteId) { this.restauranteId = restauranteId; }

    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public void setEnderecoEntrega(Endereco enderecoEntrega) { this.enderecoEntrega = enderecoEntrega; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public LocalDateTime getDataPedido() { return dataPedido; }
    public void setDataPedido(LocalDateTime dataPedido) { this.dataPedido = dataPedido; }

    public List<ItemPedidoResponse> getItens() { return itens; }
    public void setItens(List<ItemPedidoResponse> itens) { this.itens = itens; }
}