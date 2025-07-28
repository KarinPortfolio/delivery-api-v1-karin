package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.UsuarioRequest;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.service.UsuarioService;
import com.deliverytech.api.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerSimpleTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    // Array de dados mockados para diferentes cenários
    private Usuario[] usuariosMock;
    private UsuarioRequest[] requestsMock;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Mock data arrays
        usuariosMock = new Usuario[]{
            criarUsuarioMock(1L, "Admin User", "admin@test.com", Role.ADMIN, true),
            criarUsuarioMock(2L, "Cliente User", "cliente@test.com", Role.CLIENTE, true),
            criarUsuarioMock(3L, "Entregador User", "entregador@test.com", Role.ENTREGADOR, true),
            criarUsuarioMock(4L, "Usuario Inativo", "inativo@test.com", Role.CLIENTE, false)
        };

        requestsMock = new UsuarioRequest[]{
            criarUsuarioRequest("Novo Admin", "novo.admin@test.com", "123456", Role.ADMIN),
            criarUsuarioRequest("Novo Cliente", "novo.cliente@test.com", "123456", Role.CLIENTE),
            criarUsuarioRequest("Novo Entregador", "novo.entregador@test.com", "123456", Role.ENTREGADOR)
        };
    }

    @Test
    void deveListarTodosUsuarios() throws Exception {
        // Given - Usando array de usuários mock
        List<Usuario> usuariosList = Arrays.asList(usuariosMock);
        when(usuarioService.listarTodosUsuarios()).thenReturn(usuariosList);

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].nome").value("Admin User"))
                .andExpect(jsonPath("$[1].nome").value("Cliente User"))
                .andExpect(jsonPath("$[2].nome").value("Entregador User"))
                .andExpect(jsonPath("$[3].nome").value("Usuario Inativo"));

        verify(usuarioService).listarTodosUsuarios();
    }

    @Test
    void deveBuscarUsuarioPorId() throws Exception {
        // Given - Usando primeiro usuário do array mock
        Usuario usuario = usuariosMock[0];
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Admin User"))
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(usuarioService).buscarUsuarioPorId(1L);
    }

    @Test
    void deveRetornar404ParaUsuarioInexistente() throws Exception {
        // Given
        when(usuarioService.buscarUsuarioPorId(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(usuarioService).buscarUsuarioPorId(999L);
    }

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        // Given - Usando primeiro request do array mock
        UsuarioRequest request = requestsMock[0];
        Usuario usuarioSalvo = usuariosMock[0];
        
        when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(usuarioSalvo);

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Admin User"))
                .andExpect(jsonPath("$.email").value("admin@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(usuarioService).criarUsuario(any(Usuario.class));
    }

    @Test
    void deveTestarCriacaoComDiferentesRoles() throws Exception {
        // Given - Testando com array de diferentes roles
        for (int i = 0; i < requestsMock.length; i++) {
            UsuarioRequest request = requestsMock[i];
            Usuario usuarioSalvo = usuariosMock[i];
            
            when(usuarioService.criarUsuario(any(Usuario.class))).thenReturn(usuarioSalvo);

            // When & Then
            mockMvc.perform(post("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.role").value(usuarioSalvo.getRole().toString()));

            // Verify the mock was called before reset
            verify(usuarioService).criarUsuario(any(Usuario.class));
            
            // Reset mock para próxima iteração
            reset(usuarioService);
        }
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        // Given - Usando dados do array mock
        Usuario usuarioExistente = usuariosMock[0];
        UsuarioRequest requestAtualização = requestsMock[0];
        requestAtualização.setNome("Admin Atualizado");
        
        Usuario usuarioAtualizado = criarUsuarioMock(1L, "Admin Atualizado", "admin@test.com", Role.ADMIN, true);

        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioService.atualizarUsuario(eq(1L), any(Usuario.class))).thenReturn(usuarioAtualizado);

        // When & Then
        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestAtualização)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Admin Atualizado"));

        verify(usuarioService).atualizarUsuario(eq(1L), any(Usuario.class));
    }

    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        // Given - Usando usuário do array mock
        Usuario usuario = usuariosMock[0];
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).deletarUsuario(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(usuarioService).deletarUsuario(1L);
    }

    @Test
    void deveValidarCamposObrigatorios() throws Exception {
        // Given - UsuarioRequest com campos inválidos/vazios
        UsuarioRequest requestInvalido = new UsuarioRequest();
        requestInvalido.setNome(""); // Nome vazio
        requestInvalido.setEmail("email-invalido"); // Email inválido
        requestInvalido.setSenha("12"); // Senha muito curta

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        verify(usuarioService, never()).criarUsuario(any(Usuario.class));
    }

    @Test
    void deveTestarListagemVazia() throws Exception {
        // Given - Lista vazia
        when(usuarioService.listarTodosUsuarios()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(usuarioService).listarTodosUsuarios();
    }

    @Test
    void deveManipularExcecaoNaCriacao() throws Exception {
        // Given
        UsuarioRequest request = requestsMock[0];
        when(usuarioService.criarUsuario(any(Usuario.class)))
            .thenThrow(new RuntimeException("Erro ao salvar usuário"));

        // When & Then
        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        verify(usuarioService).criarUsuario(any(Usuario.class));
    }

    // Métodos auxiliares para criar dados mock
    private Usuario criarUsuarioMock(Long id, String nome, String email, Role role, boolean ativo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setRole(role);
        usuario.setAtivo(ativo);
        usuario.setDataCriacao(LocalDateTime.now());
        return usuario;
    }

    private UsuarioRequest criarUsuarioRequest(String nome, String email, String senha, Role role) {
        UsuarioRequest request = new UsuarioRequest();
        request.setNome(nome);
        request.setEmail(email);
        request.setSenha(senha);
        request.setRole(role);
        return request;
    }
}
