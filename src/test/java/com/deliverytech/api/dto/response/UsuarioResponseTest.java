package com.deliverytech.api.dto.response;

import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioResponseTest {

    @Test
    void deveCriarUsuarioResponseVazio() {
        // When
        UsuarioResponse response = new UsuarioResponse();
        
        // Then
        assertNull(response.getId());
        assertNull(response.getNome());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getAtivo());
        assertNull(response.getDataCriacao());
    }

    @Test
    void deveCriarUsuarioResponseComParametros() {
        // Given
        Long id = 1L;
        String nome = "João Silva";
        String email = "joao@teste.com";
        Role role = Role.CLIENTE;
        Boolean ativo = true;
        LocalDateTime dataCriacao = LocalDateTime.now();
        
        // When
        UsuarioResponse response = new UsuarioResponse(id, nome, email, role, ativo, dataCriacao);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(ativo, response.getAtivo());
        assertEquals(dataCriacao, response.getDataCriacao());
    }

    @Test
    void deveCriarUsuarioResponseAPartirDeUsuario() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId(2L);
        usuario.setNome("Maria Santos");
        usuario.setEmail("maria@teste.com");
        usuario.setRole(Role.ADMIN);
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());
        
        // When
        UsuarioResponse response = UsuarioResponse.fromEntity(usuario);
        
        // Then
        assertEquals(usuario.getId(), response.getId());
        assertEquals(usuario.getNome(), response.getNome());
        assertEquals(usuario.getEmail(), response.getEmail());
        assertEquals(usuario.getRole(), response.getRole());
        assertEquals(usuario.getAtivo(), response.getAtivo());
        assertEquals(usuario.getDataCriacao(), response.getDataCriacao());
    }

    @Test
    void deveDefinirEObterTodosOsCampos() {
        // Given
        UsuarioResponse response = new UsuarioResponse();
        Long id = 5L;
        String nome = "Pedro Costa";
        String email = "pedro@teste.com";
        Role role = Role.ENTREGADOR;
        Boolean ativo = false;
        LocalDateTime dataCriacao = LocalDateTime.now();
        
        // When
        response.setId(id);
        response.setNome(nome);
        response.setEmail(email);
        response.setRole(role);
        response.setAtivo(ativo);
        response.setDataCriacao(dataCriacao);
        
        // Then
        assertEquals(id, response.getId());
        assertEquals(nome, response.getNome());
        assertEquals(email, response.getEmail());
        assertEquals(role, response.getRole());
        assertEquals(ativo, response.getAtivo());
        assertEquals(dataCriacao, response.getDataCriacao());
    }

    @Test
    void deveTestarTodasAsRoles() {
        // Given
        Role[] roles = {Role.ADMIN, Role.CLIENTE, Role.ENTREGADOR};
        
        for (Role role : roles) {
            // When
            UsuarioResponse response = new UsuarioResponse(1L, "Teste", "teste@email.com", role, true, LocalDateTime.now());
            
            // Then
            assertEquals(role, response.getRole());
        }
    }

    @Test
    void deveManterConsistenciaComStatusAtivo() {
        // Given
        UsuarioResponse responseAtivo = new UsuarioResponse();
        UsuarioResponse responseInativo = new UsuarioResponse();
        
        // When
        responseAtivo.setAtivo(true);
        responseInativo.setAtivo(false);
        
        // Then
        assertTrue(responseAtivo.getAtivo());
        assertFalse(responseInativo.getAtivo());
    }

    @Test
    void devePermitirValoresNulos() {
        // Given
        UsuarioResponse response = new UsuarioResponse();
        
        // When - definindo valores nulos
        response.setId(null);
        response.setNome(null);
        response.setEmail(null);
        response.setRole(null);
        response.setAtivo(null);
        response.setDataCriacao(null);
        
        // Then
        assertNull(response.getId());
        assertNull(response.getNome());
        assertNull(response.getEmail());
        assertNull(response.getRole());
        assertNull(response.getAtivo());
        assertNull(response.getDataCriacao());
    }
}
