package com.deliverytech.api.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        // Limpar o banco de dados antes de cada teste para garantir isolamento
        entityManager.clear();

        // Criar e persistir usuários para os testes
        usuario1 = new Usuario();
        usuario1.setNome("João Cliente");
        usuario1.setEmail("joao.cliente@example.com");
        usuario1.setSenha("senhaHash1"); // Senha já codificada para simular o que viria do serviço
        usuario1.setRole(Role.CLIENTE);
        usuario1.setAtivo(true);
        entityManager.persistAndFlush(usuario1);

        usuario2 = new Usuario();
        usuario2.setNome("Maria Admin");
        usuario2.setEmail("maria.admin@example.com");
        usuario2.setSenha("senhaHash2");
        usuario2.setRole(Role.ADMIN);
        usuario2.setAtivo(true);
        entityManager.persistAndFlush(usuario2);
    }

    // --- Testes para findByEmail(String email) ---

    @Test
    @DisplayName("Deve encontrar usuário por email existente")
    void deveEncontrarUsuarioPorEmailExistente() {
        // Act
        Optional<Usuario> foundUsuario = usuarioRepository.findByEmail("joao.cliente@example.com");

        // Assert
        assertTrue(foundUsuario.isPresent());
        assertEquals("João Cliente", foundUsuario.get().getNome());
        assertEquals("joao.cliente@example.com", foundUsuario.get().getEmail());
        assertEquals(Role.CLIENTE, foundUsuario.get().getRole());
    }

    @Test
    @DisplayName("Não deve encontrar usuário por email inexistente")
    void naoDeveEncontrarUsuarioPorEmailInexistente() {
        // Act
        Optional<Usuario> foundUsuario = usuarioRepository.findByEmail("naoexiste@example.com");

        // Assert
        assertFalse(foundUsuario.isPresent());
    }

    // --- Testes para métodos herdados de JpaRepository (básicos) ---

    @Test
    @DisplayName("Deve salvar um usuário corretamente")
    void deveSalvarUsuarioCorretamente() {
        // Arrange
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome("Novo Usuário Teste");
        novoUsuario.setEmail("novo.usuario@example.com");
        novoUsuario.setSenha("novaSenhaHash");
        novoUsuario.setRole(Role.ENTREGADOR);
        novoUsuario.setAtivo(false);

        // Act
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Assert
        assertNotNull(usuarioSalvo.getId());
        assertEquals("Novo Usuário Teste", usuarioSalvo.getNome());
        assertEquals("novo.usuario@example.com", usuarioSalvo.getEmail());
        assertEquals(Role.ENTREGADOR, usuarioSalvo.getRole());
        assertFalse(usuarioSalvo.getAtivo());

        // Verificar se foi salvo no banco de dados
        Usuario usuarioEncontrado = entityManager.find(Usuario.class, usuarioSalvo.getId());
        assertNotNull(usuarioEncontrado);
        assertEquals("Novo Usuário Teste", usuarioEncontrado.getNome());
    }

    @Test
    @DisplayName("Deve encontrar usuário por ID existente")
    void deveEncontrarUsuarioPorIdExistente() {
        // Act
        Optional<Usuario> foundUsuario = usuarioRepository.findById(usuario1.getId());

        // Assert
        assertTrue(foundUsuario.isPresent());
        assertEquals("João Cliente", foundUsuario.get().getNome());
    }

    @Test
    @DisplayName("Não deve encontrar usuário por ID inexistente")
    void naoDeveEncontrarUsuarioPorIdInexistente() {
        // Act
        Optional<Usuario> foundUsuario = usuarioRepository.findById(999L); // ID que não existe

        // Assert
        assertFalse(foundUsuario.isPresent());
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    void deveRetornarTodosOsUsuarios() {
        // Act
        List<Usuario> allUsuarios = usuarioRepository.findAll();

        // Assert
        assertNotNull(allUsuarios);
        assertEquals(2, allUsuarios.size());
        assertTrue(allUsuarios.stream().anyMatch(u -> u.getNome().equals("João Cliente")));
        assertTrue(allUsuarios.stream().anyMatch(u -> u.getNome().equals("Maria Admin")));
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver usuários")
    void deveRetornarListaVaziaSeNaoHouverUsuarios() {
        // Arrange
        entityManager.clear(); // Limpa os usuários persistidos no setUp
        usuarioRepository.deleteAll(); // Garante que não há usuários no banco antes do teste

        // Act
        List<Usuario> allUsuarios = usuarioRepository.findAll();

        // Assert
        assertNotNull(allUsuarios);
        assertTrue(allUsuarios.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um usuário por ID existente")
    void deveDeletarUsuarioPorIdExistente() {
        // Arrange
        Long idToDelete = usuario1.getId();
        assertTrue(usuarioRepository.findById(idToDelete).isPresent()); // Verifica que existe antes de deletar

        // Act
        usuarioRepository.deleteById(idToDelete);
        entityManager.flush(); // Garante que a operação de delete foi sincronizada

        // Assert
        assertFalse(usuarioRepository.findById(idToDelete).isPresent()); // Verifica que não existe mais
    }

    @Test
    @DisplayName("Não deve lançar exceção ao tentar deletar usuário por ID inexistente")
    void naoDeveLancarExcecaoAoDeletarUsuarioInexistente() {
        // Act & Assert
        assertDoesNotThrow(() -> usuarioRepository.deleteById(999L)); // Tenta deletar um ID que não existe
        // O JpaRepository.deleteById não lança exceção se o ID não for encontrado.
    }
}
