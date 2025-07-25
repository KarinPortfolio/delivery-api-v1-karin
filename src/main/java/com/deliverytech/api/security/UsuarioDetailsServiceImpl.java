package com.deliverytech.api.security;

import java.util.Collections;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            System.out.println("Tentando carregar usuário com email: " + email);
            
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

            System.out.println("Usuário encontrado: " + usuario.getEmail() + ", Ativo: " + usuario.getAtivo());

            if (!usuario.getAtivo()) {
                throw new UsernameNotFoundException("Usuário inativo: " + email);
            }

            return new User(
                    usuario.getEmail(),
                    usuario.getSenha(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole())));
        } catch (Exception e) {
            System.err.println("Erro ao carregar usuário: " + e.getMessage());
            e.printStackTrace();
            throw new UsernameNotFoundException("Erro ao carregar usuário: " + email, e);
        }
    }
}