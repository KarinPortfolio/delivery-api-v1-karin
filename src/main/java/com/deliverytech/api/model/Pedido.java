package com.deliverytech.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private LocalDateTime dataPedido = LocalDateTime.now();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens;

    @Embedded
    private Endereco enderecoEntrega;

    // Construtores
    public Pedido() {}

    public Pedido(Long id, Cliente cliente, Restaurante restaurante, BigDecimal total, StatusPedido status, 
                  LocalDateTime dataPedido, List<ItemPedido> itens, Endereco enderecoEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.total = total;
        this.status = status;
        this.dataPedido = dataPedido;
        this.itens = itens;
        this.enderecoEntrega = enderecoEntrega;
    }

    // Getters manuais para resolver problemas do Lombok
    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

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

    // Setters manuais para resolver problemas do Lombok
    public void setId(Long id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void setEnderecoEntrega(Endereco enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Pedido pedido = (Pedido) obj;
        
        return java.util.Objects.equals(this.id, pedido.id) &&
               java.util.Objects.equals(this.cliente, pedido.cliente) &&
               java.util.Objects.equals(this.restaurante, pedido.restaurante) &&
               java.util.Objects.equals(this.total, pedido.total) &&
               java.util.Objects.equals(this.status, pedido.status) &&
               java.util.Objects.equals(this.dataPedido, pedido.dataPedido) &&
               java.util.Objects.equals(this.itens, pedido.itens) &&
               java.util.Objects.equals(this.enderecoEntrega, pedido.enderecoEntrega);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, cliente, restaurante, total, status, dataPedido, itens, enderecoEntrega);
    }
}