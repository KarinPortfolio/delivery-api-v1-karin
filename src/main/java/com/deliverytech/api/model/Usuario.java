package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

// Importação para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario")
@Schema(description = "Representa um usuário no sistema (cliente, admin, entregador, etc.)") // Anotação Schema na entidade
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Ana Paula")
    private String nome;

    @Column(unique = true)
    @Schema(description = "Endereço de e-mail do usuário (único)", example = "ana.paula@example.com")
    private String email;

    @Schema(description = "Senha do usuário (codificada no banco de dados)", example = "senha_hash_bcrypt")
    private String senha;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Papel (role) do usuário no sistema", example = "CLIENTE", implementation = Role.class)
    private Role role;

    @Builder.Default
    @Schema(description = "Indica se a conta do usuário está ativa", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Schema(description = "Data e hora de criação da conta do usuário", example = "2025-07-23T10:00:00")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurante_id")
    @Schema(description = "Restaurante associado ao usuário (se aplicável, ex: usuário admin de um restaurante)")
    private Restaurante restaurante;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return this.ativo; }
    
    // Getter manual para contornar problema do Lombok
    public Boolean getAtivo() {
        return this.ativo;
    }
}