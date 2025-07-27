package com.deliverytech.api.controller;

import com.deliverytech.api.model.Produto;
import com.deliverytech.api.service.ProdutoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
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
class ProdutoControllerTest {

    @Mock
    private ProdutoServiceImpl produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveListarProdutos() throws Exception {
        // Arrange
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Pizza Margherita");
        produto1.setDescricao("Pizza tradicional");
        produto1.setPreco(new BigDecimal("25.99"));
        produto1.setDisponivel(true);

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Pizza Calabresa");
        produto2.setDescricao("Pizza com calabresa");
        produto2.setPreco(new BigDecimal("29.99"));
        produto2.setDisponivel(true);

        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoService.buscarPorRestaurante(anyLong())).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/produtos")
                .param("restauranteId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$[1].nome").value("Pizza Calabresa"));
    }

    @Test
    void deveCadastrarProduto() throws Exception {
        // Arrange
        Produto produtoRequest = new Produto();
        produtoRequest.setNome("Nova Pizza");
        produtoRequest.setDescricao("Pizza nova");
        produtoRequest.setPreco(new BigDecimal("35.99"));
        produtoRequest.setDisponivel(true);

        Produto produtoResponse = new Produto();
        produtoResponse.setId(1L);
        produtoResponse.setNome("Nova Pizza");
        produtoResponse.setDescricao("Pizza nova");
        produtoResponse.setPreco(new BigDecimal("35.99"));
        produtoResponse.setDisponivel(true);

        when(produtoService.cadastrar(any(Produto.class))).thenReturn(produtoResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Nova Pizza"))
                .andExpect(jsonPath("$.preco").value(35.99));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        // Arrange
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Pizza Margherita");
        produto.setDescricao("Pizza tradicional");
        produto.setPreco(new BigDecimal("25.99"));
        produto.setDisponivel(true);

        when(produtoService.buscarPorId(1L)).thenReturn(Optional.of(produto));

        // Act & Assert
        mockMvc.perform(get("/api/v1/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$.preco").value(25.99));
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        // Arrange
        Produto produtoRequest = new Produto();
        produtoRequest.setNome("Pizza Atualizada");
        produtoRequest.setDescricao("Pizza atualizada");
        produtoRequest.setPreco(new BigDecimal("30.99"));
        produtoRequest.setDisponivel(true);

        Produto produtoResponse = new Produto();
        produtoResponse.setId(1L);
        produtoResponse.setNome("Pizza Atualizada");
        produtoResponse.setDescricao("Pizza atualizada");
        produtoResponse.setPreco(new BigDecimal("30.99"));
        produtoResponse.setDisponivel(true);

        when(produtoService.atualizar(anyLong(), any(Produto.class))).thenReturn(produtoResponse);

        // Act & Assert
        mockMvc.perform(put("/api/v1/produtos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(produtoRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Pizza Atualizada"))
                .andExpect(jsonPath("$.preco").value(30.99));
    }

    // ===== TESTES DE CASOS DE ERRO =====

    @Test
    void deveRetornar404AoBuscarProdutoInexistente() throws Exception {
        // Arrange
        when(produtoService.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/v1/produtos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveListarTodosProdutos() throws Exception {
        // Arrange
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Pizza Margherita");
        produto1.setPreco(new BigDecimal("25.99"));

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Pizza Calabresa");
        produto2.setPreco(new BigDecimal("29.99"));

        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoService.listarTodos()).thenReturn(produtos);

        // Act & Assert
        mockMvc.perform(get("/api/v1/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$[1].nome").value("Pizza Calabresa"));
    }

    @Test
    void deveAlterarDisponibilidadeProduto() throws Exception {
        // Arrange
        doNothing().when(produtoService).alterarDisponibilidade(1L, false);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/produtos/1/disponibilidade")
                .param("disponivel", "false"))
                .andExpect(status().isOk());

        verify(produtoService, times(1)).alterarDisponibilidade(1L, false);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverProdutosPorRestaurante() throws Exception {
        // Arrange
        when(produtoService.buscarPorRestaurante(1L)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/v1/produtos")
                .param("restauranteId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
