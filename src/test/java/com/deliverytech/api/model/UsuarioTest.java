package com.deliverytech.api.model;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class UsuarioTest {

    private Usuario usuario;
    private Restaurante restauranteMock;

    @BeforeEach
  
    void setUp() {
        // Inicializa um objeto Usuario antes de cada teste
        usuario = new Usuario();
        // Cria um mock ou stub para a entidade Restaurante, se necessário
        restauranteMock = new Restaurante();
        restauranteMock.setId(1L);
        restauranteMock.setNome("Restaurante Mock");
    }

    @Test
    @DisplayName("Deve criar um usuário com construtor vazio e valores padrão")
    void deveCriarUsuarioComConstrutorVazioEValoresPadrao() {
        // Assert
        assertNull(usuario.getId(), "ID deve ser nulo para um novo usuário sem ID definido");
        assertNull(usuario.getNome(), "Nome deve ser nulo por padrão");
        assertNull(usuario.getEmail(), "Email deve ser nulo por padrão");
        assertNull(usuario.getSenha(), "Senha deve ser nula por padrão");
        assertNull(usuario.getRole(), "Role deve ser nula por padrão");
        assertTrue(usuario.getAtivo(), "Ativo deve ser true por padrão (@Builder.Default)");
        assertNotNull(usuario.getDataCriacao(), "Data de criação não deve ser nula por padrão (@Builder.Default)");
        assertTrue(usuario.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de criação deve ser aproximadamente agora");
        assertNull(usuario.getRestaurante(), "Restaurante deve ser nulo por padrão");
    }

    @Test
    @DisplayName("Deve criar um usuário usando o construtor AllArgsConstructor")
    void deveCriarUsuarioComAllArgsConstructor() {
        // Arrange
        Long id = 1L;
        String nome = "Ana Paula";
        String email = "ana.paula@example.com";
        String senha = "hashedPassword";
        Role role = Role.CLIENTE;
        Boolean ativo = true;
        LocalDateTime dataCriacao = LocalDateTime.of(2024, 1, 1, 10, 0);

        // Act
        Usuario usuarioCompleto = new Usuario(id, nome, email, senha, role, ativo, dataCriacao, restauranteMock);

        // Assert
        assertEquals(id, usuarioCompleto.getId());
        assertEquals(nome, usuarioCompleto.getNome());
        assertEquals(email, usuarioCompleto.getEmail());
        assertEquals(senha, usuarioCompleto.getSenha());
        assertEquals(role, usuarioCompleto.getRole());
        assertEquals(ativo, usuarioCompleto.getAtivo());
        assertEquals(dataCriacao, usuarioCompleto.getDataCriacao());
        assertEquals(restauranteMock, usuarioCompleto.getRestaurante());
    }

    @Test
    @DisplayName("Deve criar um usuário usando o construtor Builder")
    void deveCriarUsuarioComBuilder() {
        // Arrange
        Long id = 2L;
        String nome = "Carlos Admin";
        String email = "carlos.admin@example.com";
        String senha = "anotherHashedPassword";
        Role role = Role.ADMIN;

        // Act
        Usuario usuarioBuilder = new Usuario();
        usuarioBuilder.setId(id);
        usuarioBuilder.setNome(nome);
        usuarioBuilder.setEmail(email);
        usuarioBuilder.setSenha(senha);
        usuarioBuilder.setRole(role);
        usuarioBuilder.setAtivo(false); // Explicitamente definido
        usuarioBuilder.setDataCriacao(LocalDateTime.of(2023, 5, 10, 9, 30)); // Explicitamente definido
        usuarioBuilder.setRestaurante(restauranteMock);

        // Assert
        assertEquals(id, usuarioBuilder.getId());
        assertEquals(nome, usuarioBuilder.getNome());
        assertEquals(email, usuarioBuilder.getEmail());
        assertEquals(senha, usuarioBuilder.getSenha());
        assertEquals(role, usuarioBuilder.getRole());
        assertFalse(usuarioBuilder.getAtivo());
        assertEquals(LocalDateTime.of(2023, 5, 10, 9, 30), usuarioBuilder.getDataCriacao());
        assertEquals(restauranteMock, usuarioBuilder.getRestaurante());
    }

    @Test
    @DisplayName("Deve testar os setters e getters")
    void deveTestarSettersAndGetters() {
        // Arrange
        Long newId = 3L;
        String newName = "Mariana Entregadora";
        String newEmail = "mariana.e@example.com";
        String newSenha = "newHashedPassword";
        Role newRole = Role.ENTREGADOR;
        Boolean newAtivo = false;
        LocalDateTime newDataCriacao = LocalDateTime.of(2022, 11, 20, 15, 45);
        Restaurante newRestauranteMock = new Restaurante();
        newRestauranteMock.setId(2L);
        newRestauranteMock.setNome("Outro Restaurante");


        // Act
        usuario.setId(newId);
        usuario.setNome(newName);
        usuario.setEmail(newEmail);
        usuario.setSenha(newSenha);
        usuario.setRole(newRole);
        usuario.setAtivo(newAtivo);
        usuario.setDataCriacao(newDataCriacao);
        usuario.setRestaurante(newRestauranteMock);

        // Assert
        assertEquals(newId, usuario.getId());
        assertEquals(newName, usuario.getNome());
        assertEquals(newEmail, usuario.getEmail());
        assertEquals(newSenha, usuario.getSenha());
        assertEquals(newRole, usuario.getRole());
        assertEquals(newAtivo, usuario.getAtivo());
        assertEquals(newDataCriacao, usuario.getDataCriacao());
        assertEquals(newRestauranteMock, usuario.getRestaurante());
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'ativo' quando não definido no builder")
    void deveVerificarAtivoPadraoNoBuilder() {
        // Act
        Usuario usuarioSemAtivo = new Usuario();
        usuarioSemAtivo.setNome("Usuario Sem Ativo");
        usuarioSemAtivo.setEmail("sem.ativo@example.com");
        usuarioSemAtivo.setSenha("pass");
        usuarioSemAtivo.setRole(Role.CLIENTE);

        // Assert
        assertTrue(usuarioSemAtivo.getAtivo(), "Ativo deve ser true por padrão quando não especificado no builder");
    }

    @Test
    @DisplayName("Deve verificar o comportamento padrão de 'dataCriacao' quando não definido no builder")
    void deveVerificarDataCriacaoPadraoNoBuilder() {
        // Act
        Usuario usuarioSemDataCriacao = new Usuario();
        usuarioSemDataCriacao.setNome("Usuario Sem Data Criação");
        usuarioSemDataCriacao.setEmail("sem.data@example.com");
        usuarioSemDataCriacao.setSenha("pass");
        usuarioSemDataCriacao.setRole(Role.CLIENTE);

        // Assert
        assertNotNull(usuarioSemDataCriacao.getDataCriacao(), "Data de criação não deve ser nula quando não especificada no builder");
        assertTrue(usuarioSemDataCriacao.getDataCriacao().isBefore(LocalDateTime.now().plusSeconds(1)), "Data de criação deve ser aproximadamente agora");
    }

    // --- Testes para métodos UserDetails ---

    @Test
    @DisplayName("Deve retornar as authorities corretas para a role do usuário")
    void deveRetornarAuthoritiesCorretas() {
        // Arrange
        usuario.setRole(Role.CLIENTE);

        // Act
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_CLIENTE")));

        // Teste com outra role
        usuario.setRole(Role.ADMIN);
        authorities = usuario.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Deve retornar a senha correta")
    void deveRetornarSenhaCorreta() {
        // Arrange
        String expectedPassword = "mySecretPassword";
        usuario.setSenha(expectedPassword);

        // Act
        String actualPassword = usuario.getPassword();

        // Assert
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    @DisplayName("Deve retornar o email como username")
    void deveRetornarEmailComoUsername() {
        // Arrange
        String expectedEmail = "user@domain.com";
        usuario.setEmail(expectedEmail);

        // Act
        String actualUsername = usuario.getUsername();

        // Assert
        assertEquals(expectedEmail, actualUsername);
    }

    @Test
    @DisplayName("isAccountNonExpired deve sempre retornar true")
    void isAccountNonExpiredDeveRetornarTrue() {
        assertTrue(usuario.isAccountNonExpired());
    }

    @Test
    @DisplayName("isAccountNonLocked deve sempre retornar true")
    void isAccountNonLockedDeveRetornarTrue() {
        assertTrue(usuario.isAccountNonLocked());
    }

    @Test
    @DisplayName("isCredentialsNonExpired deve sempre retornar true")
    void isCredentialsNonExpiredDeveRetornarTrue() {
        assertTrue(usuario.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("isEnabled deve retornar o valor de 'ativo'")
    void isEnabledDeveRetornarAtivo() {
        // Arrange
        usuario.setAtivo(true);
        assertTrue(usuario.isEnabled());

        usuario.setAtivo(false);
        assertFalse(usuario.isEnabled());
    }

    @Test
    @DisplayName("Deve verificar a igualdade entre objetos Usuario")
    void deveVerificarIgualdadeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Usuario user1 = new Usuario(1L, "Nome", "email@test.com", "pass", Role.CLIENTE, true, now, null);
        Usuario user2 = new Usuario(1L, "Nome", "email@test.com", "pass", Role.CLIENTE, true, now, null);
        Usuario user3 = new Usuario(2L, "Nome", "email@test.com", "pass", Role.CLIENTE, true, now, null); // ID diferente
        Usuario user4 = new Usuario(1L, "Nome Diferente", "email@test.com", "pass", Role.CLIENTE, true, now, null); // Nome diferente

        // Assert (Lombok @Data gera equals e hashCode baseados em todos os campos por padrão)
        assertEquals(user1, user2, "Usuários com mesmos valores devem ser iguais");
        assertNotEquals(user1, user3, "Usuários com IDs diferentes não devem ser iguais");
        assertNotEquals(user1, user4, "Usuários com nomes diferentes não devem ser iguais");
    }

    @Test
    @DisplayName("Deve verificar o hashcode de objetos Usuario")
    void deveVerificarHashCodeDeObjetos() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        Usuario user1 = new Usuario(1L, "Nome", "email@test.com", "pass", Role.CLIENTE, true, now, null);
        Usuario user2 = new Usuario(1L, "Nome", "email@test.com", "pass", Role.CLIENTE, true, now, null);

        // Assert
        assertEquals(user1.hashCode(), user2.hashCode(), "Hashcodes de usuários iguais devem ser iguais");
    }
}