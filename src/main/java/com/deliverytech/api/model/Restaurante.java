// src/main/java/com/deliverytech/api/model/Restaurante.java

package com.deliverytech.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String telefone;
    private String categoria;
    private BigDecimal taxaEntrega;
    private Integer tempoEntregaMinutos;
    private Boolean ativo = true;
    private LocalDateTime dataCadastro = LocalDateTime.now();

    // Lombok irá gerar construtores, getters, setters, equals e hashCode automaticamente
}