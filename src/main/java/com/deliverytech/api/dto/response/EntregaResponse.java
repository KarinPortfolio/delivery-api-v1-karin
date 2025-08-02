package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.StatusEntrega;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Dados de resposta de uma entrega")
public class EntregaResponse {

    @Schema(description = "ID único da entrega", example = "1")
    private Long id;

    @Schema(description = "ID do pedido associado", example = "1")
    private Long pedidoId;

    @Schema(description = "ID do entregador responsável", example = "1")
    private Long entregadorId;

    @Schema(description = "Nome do entregador", example = "João Silva")
    private String nomeEntregador;

    @Schema(description = "Status atual da entrega")
    private StatusEntrega status;

    @Schema(description = "Endereço de entrega", example = "Rua das Flores, 123 - Centro")
    private String enderecoEntrega;

    @Schema(description = "Data e hora estimada para entrega")
    private LocalDateTime dataHoraEstimada;

    @Schema(description = "Data e hora real da entrega")
    private LocalDateTime dataHoraRealizada;

    @Schema(description = "Custo da entrega", example = "5.50")
    private BigDecimal custoEntrega;

    // Informações básicas do pedido para evitar redundância
    @Schema(description = "Valor total do pedido", example = "35.90")
    private BigDecimal valorPedido;

    @Schema(description = "Nome do cliente", example = "Maria Santos")
    private String nomeCliente;

    @Schema(description = "Nome do restaurante", example = "Pizzaria do João")
    private String nomeRestaurante;

    // Construtores
    public EntregaResponse() {}

    public EntregaResponse(Long id, Long pedidoId, Long entregadorId, String nomeEntregador, 
                          StatusEntrega status, String enderecoEntrega, LocalDateTime dataHoraEstimada, 
                          LocalDateTime dataHoraRealizada, BigDecimal custoEntrega, BigDecimal valorPedido,
                          String nomeCliente, String nomeRestaurante) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.entregadorId = entregadorId;
        this.nomeEntregador = nomeEntregador;
        this.status = status;
        this.enderecoEntrega = enderecoEntrega;
        this.dataHoraEstimada = dataHoraEstimada;
        this.dataHoraRealizada = dataHoraRealizada;
        this.custoEntrega = custoEntrega;
        this.valorPedido = valorPedido;
        this.nomeCliente = nomeCliente;
        this.nomeRestaurante = nomeRestaurante;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getEntregadorId() {
        return entregadorId;
    }

    public void setEntregadorId(Long entregadorId) {
        this.entregadorId = entregadorId;
    }

    public String getNomeEntregador() {
        return nomeEntregador;
    }

    public void setNomeEntregador(String nomeEntregador) {
        this.nomeEntregador = nomeEntregador;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public LocalDateTime getDataHoraEstimada() {
        return dataHoraEstimada;
    }

    public void setDataHoraEstimada(LocalDateTime dataHoraEstimada) {
        this.dataHoraEstimada = dataHoraEstimada;
    }

    public LocalDateTime getDataHoraRealizada() {
        return dataHoraRealizada;
    }

    public void setDataHoraRealizada(LocalDateTime dataHoraRealizada) {
        this.dataHoraRealizada = dataHoraRealizada;
    }

    public BigDecimal getCustoEntrega() {
        return custoEntrega;
    }

    public void setCustoEntrega(BigDecimal custoEntrega) {
        this.custoEntrega = custoEntrega;
    }

    public BigDecimal getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(BigDecimal valorPedido) {
        this.valorPedido = valorPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
    }
}
