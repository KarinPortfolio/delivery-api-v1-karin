package com.deliverytech.api.model;

import jakarta.persistence.*;
import lombok.*; // Mantenha lombok.* para as outras anotações
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
// REMOVA @Data AQUI!
@Getter // Gerar apenas getters
@Setter // Gerar apenas setters
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // <-- IMPORTANTE: Inclua APENAS campos específicos para equals/hashCode
@ToString(onlyExplicitlyIncluded = true) // <-- IMPORTANTE: Inclua APENAS campos específicos para toString
@Table(name = "usuario")
@Schema(description = "Representa um usuário no sistema (cliente, admin, entregador, etc.)")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do usuário", example = "1")
    @EqualsAndHashCode.Include // <-- Incluir ID no equals/hashCode
    @ToString.Include // <-- Incluir ID no toString
    private Long id;

    @Schema(description = "Nome completo do usuário", example = "Ana Paula")
    @ToString.Include
    private String nome;

    @Column(unique = true)
    @Schema(description = "Endereço de e-mail do usuário (único)", example = "ana.paula@example.com")
    @EqualsAndHashCode.Include // <-- Incluir email no equals/hashCode (é único)
    @ToString.Include
    private String email;

    @Schema(description = "Senha do usuário (codificada no banco de dados)", example = "senha_hash_bcrypt")
    private String senha; // Não inclua senha em equals/hashCode/toString por segurança e performance

    @Enumerated(EnumType.STRING)
    @Schema(description = "Papel (role) do usuário no sistema", example = "CLIENTE", implementation = Role.class)
    @ToString.Include
    private Role role;

    @Builder.Default
    @Schema(description = "Indica se a conta do usuário está ativa", example = "true")
    private Boolean ativo = true;

    @Builder.Default
    @Schema(description = "Data e hora de criação da conta do usuário", example = "2025-07-23T10:00:00")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @JsonIgnore
    @Schema(description = "Restaurante associado ao usuário (se aplicável, ex: usuário admin de um restaurante)")
    // Não adicione @EqualsAndHashCode.Include ou @ToString.Include aqui! Ele já será excluído pelo onlyExplicitlyIncluded
    private Restaurante restaurante;

    // ... (Seus métodos UserDetails permanecem os mesmos) ...

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
}