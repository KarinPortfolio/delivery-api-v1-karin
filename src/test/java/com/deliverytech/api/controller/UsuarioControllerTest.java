package com.deliverytech.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.service.UsuarioService;
import com.deliverytech.api.exception.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os usuários e retornar 200 OK")
    void deveListarTodosOsUsuarios() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setRole(Role.CLIENTE);
        
        when(usuarioService.listarTodosUsuarios()).thenReturn(Arrays.asList(usuario));

        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(usuarioService).listarTodosUsuarios();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID existente")
    void deveBuscarUsuarioPorId() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");
        usuario.setRole(Role.CLIENTE);
        
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

        // Act & Assert - testando com ID 1 que pode existir
        mockMvc.perform(get("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(usuarioService).buscarUsuarioPorId(1L);
    }
}
