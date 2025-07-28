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

import com.deliverytech.api.model.Entrega;
import com.deliverytech.api.repository.EntregaRepository;
import com.deliverytech.api.exception.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class EntregaControllerTest {

    @Mock
    private EntregaRepository entregaRepository;

    @InjectMocks
    private EntregaController entregaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(entregaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void deveBuscarEntregaPorId() throws Exception {
        // Arrange
        Entrega entrega = new Entrega();
        entrega.setId(1L);
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));

        // Act & Assert
        mockMvc.perform(get("/api/v1/entregas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        
        verify(entregaRepository).findById(1L);
    }
}
