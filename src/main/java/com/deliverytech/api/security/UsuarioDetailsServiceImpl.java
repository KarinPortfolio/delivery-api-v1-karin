package com.deliverytech.api.security;

import java.util.Collections;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
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
        } catch (UsernameNotFoundException e) {
            // Re-lança UsernameNotFoundException diretamente
            throw e;
        } catch (DataAccessException e) {
            System.err.println("Erro de acesso aos dados ao carregar usuário: " + e.getMessage());
            throw new UsernameNotFoundException("Erro ao acessar dados do usuário: " + email, e);
        } catch (IllegalArgumentException e) {
            System.err.println("Argumento inválido ao carregar usuário: " + e.getMessage());
            throw new UsernameNotFoundException("Dados inválidos para usuário: " + email, e);
        }
    }
}