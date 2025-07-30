package com.deliverytech.api.controller;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.deliverytech.api.dto.request.RegisterRequest;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.RestauranteRepository;
import com.deliverytech.api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ExtendWith(MockitoExtension.class)
class AdminRegistrationTest {

    private MockMvc mockMvc;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock 
    private RestauranteRepository restauranteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Deve permitir cadastrar ADMIN sem restaurante")
    void deveCadastrarAdminSemRestaurante() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("Admin Teste");
        request.setEmail("admin@teste.com");
        request.setSenha("123456");
        request.setRole(Role.ADMIN);
        // restauranteId = null (não é obrigatório para ADMIN)

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("Admin Teste");
        usuarioSalvo.setEmail("admin@teste.com");
        usuarioSalvo.setRole(Role.ADMIN);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("senha-encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve permitir cadastrar ADMIN com restaurante")
    void deveCadastrarAdminComRestaurante() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("Admin com Restaurante");
        request.setEmail("admin.restaurante@teste.com");
        request.setSenha("123456");
        request.setRole(Role.ADMIN);
        request.setRestauranteId(1L);

        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(2L);
        usuarioSalvo.setNome("Admin com Restaurante");
        usuarioSalvo.setEmail("admin.restaurante@teste.com");
        usuarioSalvo.setRole(Role.ADMIN);
        usuarioSalvo.setRestaurante(restaurante);

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
        when(passwordEncoder.encode(anyString())).thenReturn("senha-encoded");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve exigir restaurante para RESTAURANTE")
    void deveExigirRestauranteParaRoleRestaurante() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("Usuário Restaurante");
        request.setEmail("restaurante@teste.com");
        request.setSenha("123456");
        request.setRole(Role.RESTAURANTE);
        // restauranteId = null (obrigatório para RESTAURANTE)

        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Usuário RESTAURANTE deve ter um restaurante"));
    }
}
