package com.deliverytech.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioCliente;
    private Usuario usuarioAdmin;

    @BeforeEach
    void setUp() {
        usuarioCliente = new Usuario();
        usuarioCliente.setId(1L);
        usuarioCliente.setNome("João Cliente");
        usuarioCliente.setEmail("joao.cliente@example.com");
        usuarioCliente.setSenha("encodedPassword1"); // Senha já codificada para simular o que viria do serviço
        usuarioCliente.setRole(Role.CLIENTE);
        usuarioCliente.setAtivo(true);

        usuarioAdmin = new Usuario();
        usuarioAdmin.setId(2L);
        usuarioAdmin.setNome("Maria Admin");
        usuarioAdmin.setEmail("maria.admin@example.com");
        usuarioAdmin.setSenha("encodedPassword2");
        usuarioAdmin.setRole(Role.ADMIN);
        usuarioAdmin.setAtivo(true);
    }

    // --- Testes para criarUsuario(Usuario usuario) ---

    @Test
    @DisplayName("Deve criar um novo usuário com senha codificada e retornar o usuário salvo")
    void deveCriarUsuarioComSucesso() {
        // Arrange
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Novo Usuário");
        novoUsuario.setEmail("novo.usuario@example.com");
        novoUsuario.setSenha("senhaOriginal");
        novoUsuario.setRole(Role.CLIENTE);
        novoUsuario.setAtivo(true);

        // Simula a codificação da senha
        when(passwordEncoder.encode("senhaOriginal")).thenReturn("encodedSenhaOriginal");
        // Simula o salvamento no repositório
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario userToSave = invocation.getArgument(0);
            userToSave.setId(3L); // Simula a atribuição de ID pelo banco
            return userToSave;
        });

        // Act
        Usuario resultado = usuarioService.criarUsuario(novoUsuario);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals("Novo Usuário", resultado.getNome());
        assertEquals("novo.usuario@example.com", resultado.getEmail());
        assertEquals("encodedSenhaOriginal", resultado.getSenha()); // Verifica se a senha foi codificada
        assertEquals(Role.CLIENTE, resultado.getRole());
        assertTrue(resultado.getAtivo());

        verify(passwordEncoder, times(1)).encode("senhaOriginal");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // --- Testes para listarTodosUsuarios() ---

    @Test
    @DisplayName("Deve listar todos os usuários e retornar uma lista não vazia")
    void deveListarTodosOsUsuarios() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuarioCliente, usuarioAdmin);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.listarTodosUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(usuarioCliente));
        assertTrue(resultado.contains(usuarioAdmin));
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver usuários cadastrados")
    void deveRetornarListaVaziaSeNaoHouverUsuarios() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Usuario> resultado = usuarioService.listarTodosUsuarios();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    // --- Testes para buscarUsuarioPorId(Long id) ---

    @Test
    @DisplayName("Deve buscar usuário por ID existente e retornar Optional com o usuário")
    void deveBuscarUsuarioPorIdExistente() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioCliente));

        // Act
        Optional<Usuario> resultado = usuarioService.buscarUsuarioPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(usuarioCliente.getNome(), resultado.get().getNome());
        assertEquals(usuarioCliente.getEmail(), resultado.get().getEmail());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID de usuário inexistente")
    void deveRetornarOptionalVazioParaIdInexistente() {
        // Arrange
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioService.buscarUsuarioPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findById(99L);
    }

    // --- Testes para atualizarUsuario(Long id, Usuario usuarioDetalhes) ---

    @Test
    @DisplayName("Deve atualizar um usuário existente com sucesso (sem mudar a senha)")
    void deveAtualizarUsuarioExistenteComSucessoSemMudarSenha() {
        // Arrange
        Usuario usuarioDetalhes = new Usuario();
        usuarioDetalhes.setNome("João Cliente Atualizado");
        usuarioDetalhes.setEmail("joao.cliente.atualizado@example.com");
        usuarioDetalhes.setRole(Role.CLIENTE);
        usuarioDetalhes.setAtivo(false); // Mudando status de ativo

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioCliente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.atualizarUsuario(1L, usuarioDetalhes);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Cliente Atualizado", resultado.getNome());
        assertEquals("joao.cliente.atualizado@example.com", resultado.getEmail());
        assertEquals("encodedPassword1", resultado.getSenha()); // Senha não deve mudar
        assertEquals(Role.CLIENTE, resultado.getRole());
        assertFalse(resultado.getAtivo()); // Verifica a atualização do campo ativo

        verify(usuarioRepository, times(1)).findById(1L);
        verify(passwordEncoder, never()).encode(anyString()); // Não deve codificar a senha
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve atualizar um usuário existente com sucesso (mudando a senha)")
    void deveAtualizarUsuarioExistenteComSucessoMudandoSenha() {
        // Arrange
        Usuario usuarioDetalhes = new Usuario();
        usuarioDetalhes.setNome("João Cliente");
        usuarioDetalhes.setEmail("joao.cliente@example.com");
        usuarioDetalhes.setSenha("novaSenhaOriginal"); // Nova senha
        usuarioDetalhes.setRole(Role.CLIENTE);
        usuarioDetalhes.setAtivo(true);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioCliente));
        when(passwordEncoder.encode("novaSenhaOriginal")).thenReturn("encodedNovaSenhaOriginal");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.atualizarUsuario(1L, usuarioDetalhes);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Cliente", resultado.getNome());
        assertEquals("joao.cliente@example.com", resultado.getEmail());
        assertEquals("encodedNovaSenhaOriginal", resultado.getSenha()); // Senha deve ser a nova codificada
        assertEquals(Role.CLIENTE, resultado.getRole());
        assertTrue(resultado.getAtivo());

        verify(usuarioRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("novaSenhaOriginal"); // Deve codificar a nova senha
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar atualizar usuário inexistente")
    void deveLancarRuntimeExceptionAoAtualizarUsuarioInexistente() {
        // Arrange
        Usuario usuarioDetalhes = new Usuario();
        usuarioDetalhes.setNome("Inexistente");
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.atualizarUsuario(99L, usuarioDetalhes));

        assertEquals("Usuário não encontrado para atualização", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99L);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // --- Testes para deletarUsuario(Long id) ---

    @Test
    @DisplayName("Deve deletar um usuário existente com sucesso")
    void deveDeletarUsuarioExistente() {
        // Arrange
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> usuarioService.deletarUsuario(1L));

        // Assert
        verify(usuarioRepository, times(1)).existsById(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar deletar usuário inexistente")
    void deveLancarRuntimeExceptionAoDeletarUsuarioInexistente() {
        // Arrange
        when(usuarioRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.deletarUsuario(99L));

        assertEquals("Usuário não encontrado para exclusão", exception.getMessage());
        verify(usuarioRepository, times(1)).existsById(99L);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
