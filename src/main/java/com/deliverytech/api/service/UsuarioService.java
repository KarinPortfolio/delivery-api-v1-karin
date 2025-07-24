package com.deliverytech.api.service;

import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder; 

    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, Usuario usuarioDetalhes) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para atualização"));

        // Atualiza os campos necessários
        usuarioExistente.setNome(usuarioDetalhes.getNome());
        usuarioExistente.setEmail(usuarioDetalhes.getEmail());
        usuarioExistente.setRole(usuarioDetalhes.getRole());
        usuarioExistente.setAtivo(usuarioDetalhes.getAtivo() != null ? usuarioDetalhes.getAtivo() : usuarioExistente.getAtivo()); // Exemplo de como lidar com campos booleanos

        // Se uma nova senha for fornecida, codifique e atualize
        if (usuarioDetalhes.getSenha() != null && !usuarioDetalhes.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDetalhes.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado para exclusão");
        }
        usuarioRepository.deleteById(id);
    }
}