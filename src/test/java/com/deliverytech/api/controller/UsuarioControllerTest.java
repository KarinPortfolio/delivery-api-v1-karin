package com.deliverytech.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.deliverytech.api.config.SwaggerTestSecurityConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SwaggerTestSecurityConfig.class)
@ActiveProfiles("test")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve listar todos os usuários e retornar 200 OK")
    void deveListarTodosOsUsuarios() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve buscar usuário por ID existente")
    void deveBuscarUsuarioPorId() throws Exception {
        // Act & Assert - testando com ID 1 que pode existir
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk());
    }
}
