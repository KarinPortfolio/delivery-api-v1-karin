package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.ProdutoRequest;
import com.deliverytech.api.dto.response.ProdutoResponse;
import com.deliverytech.api.exception.EntityNotFoundException;
import com.deliverytech.api.exception.GlobalExceptionHandler;
import com.deliverytech.api.service.ProdutoService;
import com.deliverytech.api.service.RestauranteService;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;
    
    @Mock
    private RestauranteService restauranteService;

    @InjectMocks
    private ProdutoController produtoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produtoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveListarProdutosPorRestauranteComSucesso() throws Exception {
        // Given
        Long restauranteId = 1L;
        List<Produto> produtos = Arrays.asList(
            criarProduto(1L, "Pizza Margherita", BigDecimal.valueOf(25.00)),
            criarProduto(2L, "Pizza Calabresa", BigDecimal.valueOf(30.00))
        );

        when(produtoService.buscarPorRestaurante(restauranteId)).thenReturn(produtos);

        // When & Then
        mockMvc.perform(get("/api/v1/produtos/restaurante/{id}", restauranteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Pizza Margherita"))
                .andExpect(jsonPath("$[1].nome").value("Pizza Calabresa"));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() throws Exception {
        // Given
        Long produtoId = 1L;
        Produto produto = criarProduto(produtoId, "Pizza Margherita", BigDecimal.valueOf(25.00));

        when(produtoService.buscarPorId(produtoId)).thenReturn(Optional.of(produto));

        // When & Then
        mockMvc.perform(get("/api/v1/produtos/{id}", produtoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Pizza Margherita"));
    }

    @Test
    void deveCriarProdutoComSucesso() throws Exception {
        // Given
        ProdutoRequest request = criarProdutoRequest();
        Produto produto = criarProduto(1L, "Pizza Margherita", BigDecimal.valueOf(25.00));
        Restaurante restaurante = new Restaurante();
        restaurante.setId(request.getRestauranteId());

        when(restauranteService.buscarPorId(request.getRestauranteId())).thenReturn(Optional.of(restaurante));
        when(produtoService.cadastrar(any(Produto.class))).thenReturn(produto);

        // When & Then
        mockMvc.perform(post("/api/v1/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Pizza Margherita"));
    }

    @Test
    void deveRetornarNotFoundQuandoProdutoNaoExistir() throws Exception {
        // Given
        Long produtoId = 999L;
        when(produtoService.buscarPorId(produtoId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/v1/produtos/{id}", produtoId))
                .andExpect(status().isNotFound());
    }

    private Produto criarProduto(Long id, String nome, BigDecimal preco) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(preco);
        return produto;
    }

    private ProdutoResponse criarProdutoResponse(Long id, String nome, BigDecimal preco) {
        ProdutoResponse response = new ProdutoResponse();
        response.setId(id);
        response.setNome(nome);
        response.setPreco(preco);
        return response;
    }

    private ProdutoRequest criarProdutoRequest() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Pizza Margherita");
        request.setCategoria("Pizza");
        request.setDescricao("Clássica italiana com tomate e mussarela");
        request.setPreco(BigDecimal.valueOf(25.00));
        request.setRestauranteId(1L);
        request.setDisponivel(true);
        return request;
    }
}
