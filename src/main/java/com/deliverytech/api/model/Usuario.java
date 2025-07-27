package com.deliverytech.api.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean ativo = true;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;

    // Construtores
    public Usuario() {}

    public Usuario(String nome, String email, String senha, Role role) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
    }

    public Usuario(Long id, String nome, String email, String senha, Role role, Boolean ativo, LocalDateTime dataCriacao, Restaurante restaurante) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
        this.restaurante = restaurante;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public Role getRole() { return role; }
    public Boolean getAtivo() { return ativo; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public Restaurante getRestaurante() { return restaurante; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setRole(Role role) { this.role = role; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public void setRestaurante(Restaurante restaurante) { this.restaurante = restaurante; }

    // Implementação do UserDetails
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Usuario usuario = (Usuario) obj;
        
        return java.util.Objects.equals(this.id, usuario.id) &&
               java.util.Objects.equals(this.nome, usuario.nome) &&
               java.util.Objects.equals(this.email, usuario.email) &&
               java.util.Objects.equals(this.senha, usuario.senha) &&
               java.util.Objects.equals(this.role, usuario.role) &&
               java.util.Objects.equals(this.ativo, usuario.ativo) &&
               java.util.Objects.equals(this.dataCriacao, usuario.dataCriacao) &&
               java.util.Objects.equals(this.restaurante, usuario.restaurante);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, nome, email, senha, role, ativo, dataCriacao, restaurante);
    }
}