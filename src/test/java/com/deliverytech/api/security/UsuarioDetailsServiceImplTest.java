package com.deliverytech.api.security;

import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioDetailsServiceImpl usuarioDetailsService;

    @BeforeEach
    void setUp() {
        usuarioDetailsService = new UsuarioDetailsServiceImpl(usuarioRepository);
    }

    @Test
    void deveCarregarUsuarioComSucesso() {
        // Given
        String email = "usuario@teste.com";
        Usuario usuario = criarUsuarioMock(email, Role.CLIENTE, true);
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // When
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENTE")));
    }

    @Test
    void deveCarregarUsuarioAdmin() {
        // Given
        String email = "admin@teste.com";
        Usuario usuario = criarUsuarioMock(email, Role.ADMIN, true);
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // When
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void deveCarregarUsuarioEntregador() {
        // Given
        String email = "entregador@teste.com";
        Usuario usuario = criarUsuarioMock(email, Role.ENTREGADOR, true);
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // When
        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

        // Then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ENTREGADOR")));
    }

    @Test
    void deveCarregarUsuarioInativo() {
        // Given
        String email = "inativo@teste.com";
        Usuario usuario = criarUsuarioMock(email, Role.CLIENTE, false);
        
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email)
        );
        
        assertTrue(exception.getMessage().contains("Usuário inativo"));
        assertTrue(exception.getMessage().contains(email));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Given
        String email = "inexistente@teste.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email)
        );
        
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        assertTrue(exception.getMessage().contains(email));
    }

    @Test
    void deveLancarExcecaoQuandoOcorreErroDeAcessoADados() {
        // Given
        String email = "usuario@teste.com";
        when(usuarioRepository.findByEmail(email))
                .thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email)
        );
        
        assertTrue(exception.getMessage().contains("Erro ao carregar usuário"));
        assertTrue(exception.getMessage().contains(email));
    }

    @Test
    void deveProcessarEmailNulo() {
        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(null)
        );
        
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void deveProcessarEmailVazio() {
        // Given
        String email = "";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email)
        );
        
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void deveProcessarEmailComEspacos() {
        // Given
        String email = "  usuario@teste.com  ";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email)
        );
        
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void deveCarregarUsuariosComDiferentesRoles() {
        // Given
        Role[] roles = {Role.CLIENTE, Role.ADMIN, Role.ENTREGADOR};
        
        for (Role role : roles) {
            String email = role.name().toLowerCase() + "@teste.com";
            Usuario usuario = criarUsuarioMock(email, role, true);
            
            when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

            // When
            UserDetails userDetails = usuarioDetailsService.loadUserByUsername(email);

            // Then
            assertNotNull(userDetails);
            assertEquals(email, userDetails.getUsername());
            assertTrue(userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role.name())));
        }
    }

    @Test
    void deveManterConsistenciaDeEstadoDoUsuario() {
        // Given
        String email = "usuario@teste.com";
        Usuario usuarioAtivo = criarUsuarioMock(email, Role.CLIENTE, true);
        Usuario usuarioInativo = criarUsuarioMock(email, Role.CLIENTE, false);
        
        // Test usuário ativo
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioAtivo));
        UserDetails userDetailsAtivo = usuarioDetailsService.loadUserByUsername(email);
        assertTrue(userDetailsAtivo.isEnabled());
        
        // Test usuário inativo - deve lançar exceção
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioInativo));
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioDetailsService.loadUserByUsername(email)
        );
        assertTrue(exception.getMessage().contains("Usuário inativo"));
    }

    // Método auxiliar para criar usuário mock
    private Usuario criarUsuarioMock(String email, Role role, boolean ativo) {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");
        usuario.setEmail(email);
        usuario.setSenha("senhaHash123");
        usuario.setRole(role);
        usuario.setAtivo(ativo);
        usuario.setDataCriacao(LocalDateTime.now());
        return usuario;
    }
}
