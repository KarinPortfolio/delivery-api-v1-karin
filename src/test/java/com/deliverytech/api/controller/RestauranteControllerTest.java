package com.deliverytech.api.controller;

import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.dto.request.RestauranteRequest;
import com.deliverytech.api.service.RestauranteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestauranteControllerTest {

    @Mock
    private RestauranteServiceImpl restauranteService;

    @InjectMocks
    private RestauranteController restauranteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void deveListarRestaurantes() throws Exception {
        // Arrange
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Restaurante Italiano");
        restaurante1.setAtivo(true);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Pizzaria Mario");
        restaurante2.setAtivo(true);

        List<Restaurante> restaurantes = Arrays.asList(restaurante1, restaurante2);
        when(restauranteService.listarTodos()).thenReturn(restaurantes);

        // Act & Assert
        mockMvc.perform(get("/api/v1/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Restaurante Italiano"))
                .andExpect(jsonPath("$[1].nome").value("Pizzaria Mario"));
    }

    @Test
    void deveCadastrarRestaurante() throws Exception {
        // Arrange
        RestauranteRequest restauranteRequest = new RestauranteRequest();
        restauranteRequest.setNome("Novo Restaurante");
        restauranteRequest.setTelefone("11999999999");
        restauranteRequest.setCategoria("Italiana");
        restauranteRequest.setTaxaEntrega(new java.math.BigDecimal("5.00"));
        restauranteRequest.setTempoEntregaMinutos(30);

        Restaurante restauranteResponse = new Restaurante();
        restauranteResponse.setId(1L);
        restauranteResponse.setNome("Novo Restaurante");
        restauranteResponse.setAtivo(true);

        when(restauranteService.cadastrar(any(Restaurante.class))).thenReturn(restauranteResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Novo Restaurante"));
    }

    @Test
    void deveBuscarRestaurantePorId() throws Exception {
        // Arrange
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");
        restaurante.setAtivo(true);

        when(restauranteService.buscarPorId(1L)).thenReturn(Optional.of(restaurante));

        // Act & Assert
        mockMvc.perform(get("/api/v1/restaurantes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Restaurante Teste"));
    }

    @Test
    void deveAtualizarRestaurante() throws Exception {
        // Arrange
        RestauranteRequest restauranteRequest = new RestauranteRequest();
        restauranteRequest.setNome("Restaurante Atualizado");
        restauranteRequest.setTelefone("11999999999");
        restauranteRequest.setCategoria("Italiana");
        restauranteRequest.setTaxaEntrega(new BigDecimal("5.00"));
        restauranteRequest.setTempoEntregaMinutos(30);

        Restaurante restauranteResponse = new Restaurante();
        restauranteResponse.setId(1L);
        restauranteResponse.setNome("Restaurante Atualizado");
        restauranteResponse.setAtivo(true);

        when(restauranteService.atualizar(anyLong(), any(Restaurante.class))).thenReturn(restauranteResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/restaurantes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Restaurante Atualizado"));
    }

    // ===== TESTES DE CASOS DE ERRO =====

    @Test
    void deveRetornar404AoBuscarRestauranteInexistente() throws Exception {
        // Arrange
        when(restauranteService.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/restaurantes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarRestaurante() throws Exception {
        // Arrange
        doNothing().when(restauranteService).deletar(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/restaurantes/1"))
                .andExpect(status().isNoContent());

        verify(restauranteService, times(1)).deletar(1L);
    }

    @Test
    void deveRetornar404AoDeletarRestauranteInexistente() throws Exception {
        // Arrange
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado"))
                .when(restauranteService).deletar(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/restaurantes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverRestaurantes() throws Exception {
        // Arrange
        when(restauranteService.listarTodos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deveRetornar409AoCadastrarRestauranteComNomeDuplicado() throws Exception {
        // Arrange
        RestauranteRequest restauranteRequest = new RestauranteRequest();
        restauranteRequest.setNome("Restaurante Existente");
        restauranteRequest.setTelefone("11999999999");
        restauranteRequest.setCategoria("Italiana");
        restauranteRequest.setTaxaEntrega(new BigDecimal("5.00"));
        restauranteRequest.setTempoEntregaMinutos(30);

        when(restauranteService.findByNome("Restaurante Existente")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restauranteRequest)))
                .andExpect(status().isConflict());
    }
}
