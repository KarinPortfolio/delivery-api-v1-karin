package com.deliverytech.api.controller;

import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.service.ClienteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteServiceImpl clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveListarClientes() throws Exception {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@example.com");
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Maria Santos");
        cliente2.setEmail("maria@example.com");
        cliente2.setAtivo(true);

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteService.listarAtivos()).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].nome").value("Maria Santos"));
    }

    @Test
    void deveCadastrarCliente() throws Exception {
        // Arrange
        Cliente clienteRequest = new Cliente();
        clienteRequest.setNome("Teste Cliente");
        clienteRequest.setEmail("teste@teste.com");

        Cliente clienteResponse = new Cliente();
        clienteResponse.setId(1L);
        clienteResponse.setNome("Teste Cliente");
        clienteResponse.setEmail("teste@teste.com");
        clienteResponse.setAtivo(true);

        when(clienteService.cadastrar(any(Cliente.class))).thenReturn(clienteResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Teste Cliente"))
                .andExpect(jsonPath("$.email").value("teste@teste.com"));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        // Arrange  
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setAtivo(true);

        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        // Act & Assert
        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        // Arrange
        Cliente clienteRequest = new Cliente();
        clienteRequest.setNome("Cliente Atualizado");
        clienteRequest.setEmail("atualizado@teste.com");

        Cliente clienteResponse = new Cliente();
        clienteResponse.setId(1L);
        clienteResponse.setNome("Cliente Atualizado");
        clienteResponse.setEmail("atualizado@teste.com");
        clienteResponse.setAtivo(true);

        when(clienteService.atualizar(anyLong(), any(Cliente.class))).thenReturn(clienteResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Cliente Atualizado"));
    }

    // ===== TESTES DE CASOS DE ERRO =====

    @Test
    void deveRetornar404AoBuscarClienteInexistente() throws Exception {
        // Arrange
        when(clienteService.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/clientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar409AoCadastrarClienteComEmailDuplicado() throws Exception {
        // Arrange
        Cliente clienteRequest = new Cliente();
        clienteRequest.setNome("Teste Cliente");
        clienteRequest.setEmail("teste@teste.com");

        when(clienteService.cadastrar(any(Cliente.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void deveRetornar404AoAtualizarClienteInexistente() throws Exception {
        // Arrange
        Cliente clienteRequest = new Cliente();
        clienteRequest.setNome("Cliente Inexistente");
        clienteRequest.setEmail("inexistente@teste.com");

        when(clienteService.atualizar(anyLong(), any(Cliente.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Act & Assert
        mockMvc.perform(put("/api/v1/clientes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverClientes() throws Exception {
        // Arrange
        when(clienteService.listarAtivos()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deveAtivarDesativarCliente() throws Exception {
        // Arrange
        doNothing().when(clienteService).ativarDesativar(1L);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/clientes/1/ativar-desativar"))
                .andExpect(status().isOk());

        verify(clienteService, times(1)).ativarDesativar(1L);
    }

    @Test
    void deveRetornar404AoAtivarDesativarClienteInexistente() throws Exception {
        // Arrange
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"))
                .when(clienteService).ativarDesativar(999L);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/clientes/999/ativar-desativar"))
                .andExpect(status().isNotFound());
    }
}
