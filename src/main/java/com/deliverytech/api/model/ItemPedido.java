package com.deliverytech.api.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    // Construtores
    public ItemPedido() {}

    public ItemPedido(Long id, Pedido pedido, Produto produto, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters manuais para resolver problemas do Lombok
    public Long getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    // Setters manuais para resolver problemas do Lombok
    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemPedido itemPedido = (ItemPedido) obj;
        
        return java.util.Objects.equals(this.id, itemPedido.id) &&
               java.util.Objects.equals(this.pedido, itemPedido.pedido) &&
               java.util.Objects.equals(this.produto, itemPedido.produto) &&
               java.util.Objects.equals(this.quantidade, itemPedido.quantidade) &&
               java.util.Objects.equals(this.precoUnitario, itemPedido.precoUnitario);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, pedido, produto, quantidade, precoUnitario);
    }
}