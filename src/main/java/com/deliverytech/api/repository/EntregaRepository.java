package com.deliverytech.api.repository;

import com.deliverytech.api.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {
    
    // Buscar entregas por pedido
    List<Entrega> findByPedido_Id(Long pedidoId);
    
    // Buscar entregas por entregador
    List<Entrega> findByEntregador_Id(Long entregadorId);
    
    // Deletar entregas por pedido ID
    @Modifying
    @Transactional
    @Query("DELETE FROM Entrega e WHERE e.pedido.id = :pedidoId")
    void deleteByPedido_Id(@Param("pedidoId") Long pedidoId);
}