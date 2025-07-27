package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.StatusPedido;
import com.deliverytech.api.model.Endereco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private Long restauranteId;
    private Endereco enderecoEntrega;
    private BigDecimal total;
    private StatusPedido status;
    private LocalDateTime dataPedido;
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