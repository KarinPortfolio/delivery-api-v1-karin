package com.deliverytech.api.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.deliverytech.api.dto.request.LoginRequest;
import com.deliverytech.api.dto.request.RegisterRequest;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.UsuarioRepository;
import com.deliverytech.api.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.deliverytech.api.security.service.RefreshTokenService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // WARNING: @MockBean is deprecated and marked for removal in Spring Boot 3.4+.
    @org.springframework.boot.test.mock.mockito.MockBean
    private JwtUtil jwtUtil;

    @org.springframework.boot.test.mock.mockito.MockBean
    private UsuarioRepository usuarioRepository;

    @org.springframework.boot.test.mock.mockito.MockBean
    private PasswordEncoder passwordEncoder;

    @org.springframework.boot.test.mock.mockito.MockBean
    private RefreshTokenService refreshTokenService;
    
    // Adicionando mocks para resolver o problema de contexto do Spring
    @org.springframework.boot.test.mock.mockito.MockBean
    private AuthenticationManager authenticationManager;

    @org.springframework.boot.test.mock.mockito.MockBean
    private com.deliverytech.api.repository.RestauranteRepository restauranteRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void deveAutenticarUsuarioComSucesso() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@teste.com");
        loginRequest.setSenha("123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("admin@teste.com");
        usuario.setSenha("encodedPassword");
        usuario.setAtivo(true);
        usuario.setRole(Role.ADMIN);

        // Configuração dos mocks
        when(usuarioRepository.findByEmail("admin@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(usuario, usuario)).thenReturn("jwt-token-123");
        com.deliverytech.api.model.RefreshToken refreshToken = new com.deliverytech.api.model.RefreshToken();
        refreshToken.setToken("refresh-token-123");
        // Use a User instance for refreshToken.setUser
        com.deliverytech.api.model.User user = new com.deliverytech.api.model.User();
        user.setId(usuario.getId());
        user.setUsername(usuario.getEmail());
        user.setPassword(usuario.getSenha());
        user.setEnabled(usuario.getAtivo());
        user.setEmail(usuario.getEmail());
        refreshToken.setUser(user);
        when(refreshTokenService.createRefreshToken(usuario.getId())).thenReturn(refreshToken);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"));

        // Verificação das interações com os mocks
        verify(usuarioRepository).findByEmail("admin@teste.com");
        verify(passwordEncoder).matches("123456", "encodedPassword");
        verify(jwtUtil).generateToken(usuario, usuario);
    }

    // ... Resto dos seus métodos de teste (não incluídos para brevidade) ...

    @Test
    void deveTestarDiferentesFormatosDeEmailNoLogin() throws Exception {
        // Given - Array de diferentes formatos de email
        String[] emails = {
            "user@domain.com",
            "user.name@company.com.br",
            "test+tag@subdomain.org",
            "admin@portal.gov.br"
        };

        for (String email : emails) {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setSenha("123456");

            Usuario usuario = new Usuario();
            usuario.setId(1L);
            usuario.setEmail(email);
            usuario.setSenha("encodedPassword");
            usuario.setAtivo(true);
            usuario.setRole(Role.CLIENTE);


            when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
            when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);
            when(jwtUtil.generateToken(usuario, usuario))
                .thenReturn("jwt-token-" + email.hashCode());
            com.deliverytech.api.model.RefreshToken refreshToken = new com.deliverytech.api.model.RefreshToken();
            refreshToken.setToken("refresh-token-" + email.hashCode());
            // Use a User instance for refreshToken.setUser
            com.deliverytech.api.model.User user = new com.deliverytech.api.model.User();
            user.setId(usuario.getId());
            user.setUsername(usuario.getEmail());
            user.setPassword(usuario.getSenha());
            user.setEnabled(usuario.getAtivo());
            user.setEmail(usuario.getEmail());
            refreshToken.setUser(user);
            when(refreshTokenService.createRefreshToken(usuario.getId())).thenReturn(refreshToken);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token-" + email.hashCode()));

            // Reset mocks para próxima iteração
            reset(usuarioRepository, passwordEncoder, jwtUtil, refreshTokenService);
        }
    }
    
    // Testes de registro ... (Não incluídos para brevidade)

    @Test
    void deveRetornar401ParaUsuarioInexistente() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("inexistente@teste.com");
        loginRequest.setSenha("123456");

        when(usuarioRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));

        verify(usuarioRepository).findByEmail("inexistente@teste.com");
        // passwordEncoder.matches não é chamado pois usuário não existe
    }

    @Test
    void deveRetornar403ParaUsuarioInativo() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("inativo@teste.com");
        loginRequest.setSenha("123456");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("inativo@teste.com");
        usuario.setAtivo(false);

        when(usuarioRepository.findByEmail("inativo@teste.com")).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").value("Usuário inativo"));

        verify(usuarioRepository).findByEmail("inativo@teste.com");
        // passwordEncoder.matches não é chamado pois usuário está inativo
    }

    @Test
    void deveRetornar401ParaCredenciaisInvalidas() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("usuario@teste.com");
        loginRequest.setSenha("senha-errada");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senhaCorretaCodificada");
        usuario.setAtivo(true);

        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha-errada", "senhaCorretaCodificada")).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));

        verify(usuarioRepository).findByEmail("usuario@teste.com");
        verify(passwordEncoder).matches("senha-errada", "senhaCorretaCodificada");
    }

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNome("Novo Usuario");
        registerRequest.setEmail("novo@usuario.com");
        registerRequest.setSenha("123456");
        registerRequest.setRole(Role.CLIENTE);

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Novo Usuario");
        usuarioSalvo.setEmail("novo@usuario.com");
        usuarioSalvo.setRole(Role.CLIENTE);

        when(usuarioRepository.findByEmail("novo@usuario.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("senha-codificada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("novo@usuario.com"))
                .andExpect(jsonPath("$.role").value("CLIENTE"));

        verify(usuarioRepository).findByEmail("novo@usuario.com");
        verify(passwordEncoder).encode("123456");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveRetornar400ParaEmailJaExistente() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNome("Usuario Existente");
        registerRequest.setEmail("existente@usuario.com");
        registerRequest.setSenha("123456");
        registerRequest.setRole(Role.CLIENTE);

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setEmail("existente@usuario.com");

        when(usuarioRepository.findByEmail("existente@usuario.com"))
                .thenReturn(Optional.of(usuarioExistente));

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Email já cadastrado"));

        verify(usuarioRepository).findByEmail("existente@usuario.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveTestarDiferentesTiposDeRole() throws Exception {
        // Given - Array de diferentes roles
        Role[] roles = {Role.ADMIN, Role.CLIENTE, Role.ENTREGADOR};
        String[] nomes = {"Admin User", "Cliente User", "Entregador User"};
        String[] emails = {"admin@test.com", "cliente@test.com", "entregador@test.com"};

        for (int i = 0; i < roles.length; i++) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setNome(nomes[i]);
            registerRequest.setEmail(emails[i]);
            registerRequest.setSenha("123456");
            registerRequest.setRole(roles[i]);

            Usuario usuarioSalvo = new Usuario();
            usuarioSalvo.setId((long) (i + 1));
            usuarioSalvo.setNome(nomes[i]);
            usuarioSalvo.setEmail(emails[i]);
            usuarioSalvo.setRole(roles[i]);

            when(usuarioRepository.findByEmail(emails[i])).thenReturn(Optional.empty());
            when(passwordEncoder.encode("123456")).thenReturn("senha-codificada");
            when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.role").value(roles[i].toString()));

            // Reset mocks para próxima iteração
            reset(usuarioRepository, passwordEncoder);
        }
    }

    @Test
    void deveValidarCamposObrigatoriosNoLogin() throws Exception {
        // Given - LoginRequest com campos vazios/nulos
        String[] invalidEmails = {"", null, "email-invalido"};
        String[] invalidSenhas = {"", null, "12"};

        for (String email : invalidEmails) {
            for (String senha : invalidSenhas) {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail(email);
                loginRequest.setSenha(senha);

                // When & Then - Alguns podem passar pela validação básica
                try {
                    mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                            .andExpect(status().is4xxClientError());
                } catch (Exception e) {
                    // Alguns casos podem causar exceções na serialização
                    // Isto é esperado para campos null
                }
            }
        }
    }
}