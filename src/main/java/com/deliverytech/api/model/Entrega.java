// src/main/java/com/deliverytech/api/model/Entrega.java
package com.deliverytech.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime; // Se houver custo de entrega
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido; // Relacionamento com Pedido

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entregador_id")
    private Entregador entregador; // Relacionamento com Entregador

    @Enumerated(EnumType.STRING)
    private StatusEntrega status; // Você precisará de um enum StatusEntrega

    private String enderecoEntrega;

    private LocalDateTime dataHoraEstimada;

    private LocalDateTime dataHoraRealizada;

    private BigDecimal custoEntrega;

    // Constructors
    public Entrega() {}

    public Entrega(Long id, Pedido pedido, Entregador entregador, StatusEntrega status, 
                   String enderecoEntrega, LocalDateTime dataHoraEstimada, 
                   LocalDateTime dataHoraRealizada, BigDecimal custoEntrega) {
        this.id = id;
        this.pedido = pedido;
        this.entregador = entregador;
        this.status = status;
        this.enderecoEntrega = enderecoEntrega;
        this.dataHoraEstimada = dataHoraEstimada;
        this.dataHoraRealizada = dataHoraRealizada;
        this.custoEntrega = custoEntrega;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Entregador getEntregador() {
        return entregador;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public LocalDateTime getDataHoraEstimada() {
        return dataHoraEstimada;
    }

    public LocalDateTime getDataHoraRealizada() {
        return dataHoraRealizada;
    }

    public BigDecimal getCustoEntrega() {
        return custoEntrega;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setEntregador(Entregador entregador) {
        this.entregador = entregador;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public void setDataHoraEstimada(LocalDateTime dataHoraEstimada) {
        this.dataHoraEstimada = dataHoraEstimada;
    }

    public void setDataHoraRealizada(LocalDateTime dataHoraRealizada) {
        this.dataHoraRealizada = dataHoraRealizada;
    }

    public void setCustoEntrega(BigDecimal custoEntrega) {
        this.custoEntrega = custoEntrega;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Entrega entrega = (Entrega) obj;
        return java.util.Objects.equals(id, entrega.id) &&
               java.util.Objects.equals(pedido, entrega.pedido) &&
               java.util.Objects.equals(entregador, entrega.entregador) &&
               java.util.Objects.equals(status, entrega.status) &&
               java.util.Objects.equals(enderecoEntrega, entrega.enderecoEntrega) &&
               java.util.Objects.equals(dataHoraEstimada, entrega.dataHoraEstimada) &&
               java.util.Objects.equals(dataHoraRealizada, entrega.dataHoraRealizada) &&
               java.util.Objects.equals(custoEntrega, entrega.custoEntrega);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, pedido, entregador, status, enderecoEntrega, 
                                     dataHoraEstimada, dataHoraRealizada, custoEntrega);
    }
}