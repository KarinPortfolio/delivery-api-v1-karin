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

@Service
@Primary
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

            // Verificar se está ativo
            if (!usuario.getAtivo()) {
                throw new UsernameNotFoundException("Usuário inativo: " + email);
            }

            return new User(
                    usuario.getEmail(),
                    usuario.getSenha(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRole())));
                    
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Erro ao carregar usuário: " + email, e);
        }
    }
}