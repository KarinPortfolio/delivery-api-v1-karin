package com.deliverytech.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.deliverytech.api.model.Entregador;
import com.deliverytech.api.repository.EntregadorRepository;
import com.deliverytech.api.exception.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class EntregadorControllerTest {

    @Mock
    private EntregadorRepository entregadorRepository;

    @InjectMocks
    private EntregadorController entregadorController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(entregadorController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveBuscarEntregadorPorId() throws Exception {
        // Arrange
        Entregador entregador = new Entregador();
        entregador.setId(1L);
        when(entregadorRepository.findById(1L)).thenReturn(Optional.of(entregador));

        // Act & Assert
        mockMvc.perform(get("/api/v1/entregadores/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(entregadorRepository).findById(1L);
    }
}
