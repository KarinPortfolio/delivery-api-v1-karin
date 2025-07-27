package com.deliverytech.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private Produto produto1;
    private Produto produto2;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Restaurante Teste");

        produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("Pizza Margherita");
        produto1.setDescricao("Pizza tradicional");
        produto1.setPreco(new BigDecimal("25.90"));
        produto1.setCategoria("Pizza");
        produto1.setDisponivel(true);
        produto1.setRestaurante(restaurante);

        produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Refrigerante Cola");
        produto2.setDescricao("Lata de 350ml");
        produto2.setPreco(new BigDecimal("7.00"));
        produto2.setCategoria("Bebidas");
        produto2.setDisponivel(true);
        produto2.setRestaurante(restaurante);
    }

    // --- Testes para cadastrar(Produto produto) ---

    @Test
    @DisplayName("Deve cadastrar um produto com sucesso")
    void deveCadastrarProdutoComSucesso() {
        // Arrange
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto1);

        // Act
        Produto resultado = produtoService.cadastrar(produto1);

        // Assert
        assertNotNull(resultado);
        assertEquals(produto1.getNome(), resultado.getNome());
        verify(produtoRepository, times(1)).save(produto1);
    }

    // --- Testes para buscarPorId(Long id) ---

    @Test
    @DisplayName("Deve buscar produto por ID existente")
    void deveBuscarProdutoPorIdExistente() {
        // Arrange
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));

        // Act
        Optional<Produto> resultado = produtoService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(produto1.getNome(), resultado.get().getNome());
        verify(produtoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID de produto inexistente")
    void deveRetornarOptionalVazioParaIdInexistente() {
        // Arrange
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Produto> resultado = produtoService.buscarPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(produtoRepository, times(1)).findById(99L);
    }

    // --- Testes para buscarPorRestaurante(Long restauranteId) ---

    @Test
    @DisplayName("Deve buscar produtos por ID de restaurante")
    void deveBuscarProdutosPorRestauranteId() {
        // Arrange
        List<Produto> produtosDoRestaurante = Arrays.asList(produto1, produto2);
        when(produtoRepository.findByRestauranteId(restaurante.getId())).thenReturn(produtosDoRestaurante);

        // Act
        List<Produto> resultado = produtoService.buscarPorRestaurante(restaurante.getId());

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(produto1));
        assertTrue(resultado.contains(produto2));
        verify(produtoRepository, times(1)).findByRestauranteId(restaurante.getId());
    }

    @Test
    @DisplayName("Deve retornar lista vazia se o restaurante não tiver produtos")
    void deveRetornarListaVaziaSeRestauranteNaoTiverProdutos() {
        // Arrange
        when(produtoRepository.findByRestauranteId(anyLong())).thenReturn(Collections.emptyList());

        // Act
        List<Produto> resultado = produtoService.buscarPorRestaurante(99L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findByRestauranteId(99L);
    }

    // --- Testes para listarTodos() ---

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodosOsProdutos() {
        // Arrange
        List<Produto> todosOsProdutos = Arrays.asList(produto1, produto2);
        when(produtoRepository.findAll()).thenReturn(todosOsProdutos);

        // Act
        List<Produto> resultado = produtoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(produto1));
        assertTrue(resultado.contains(produto2));
        verify(produtoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver produtos cadastrados")
    void deveRetornarListaVaziaSeNaoHouverProdutosCadastrados() {
        // Arrange
        when(produtoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Produto> resultado = produtoService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(produtoRepository, times(1)).findAll();
    }

    // --- Testes para atualizar(Long id, Produto produtoAtualizado) ---

    @Test
    @DisplayName("Deve atualizar um produto existente com sucesso")
    void deveAtualizarProdutoExistenteComSucesso() {
        // Arrange
        Produto produtoParaAtualizar = new Produto();
        produtoParaAtualizar.setNome("Pizza Quatro Queijos");
        produtoParaAtualizar.setDescricao("Pizza com 4 queijos");
        produtoParaAtualizar.setPreco(new BigDecimal("35.00"));
        produtoParaAtualizar.setCategoria("Pizza");
        produtoParaAtualizar.setDisponivel(false);

        // Simula o produto existente sendo encontrado e depois salvo com as atualizações
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoParaAtualizar); // Retorna o produto com os novos dados

        // Act
        Produto resultado = produtoService.atualizar(1L, produtoParaAtualizar);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pizza Quatro Queijos", resultado.getNome());
        assertEquals(new BigDecimal("35.00"), resultado.getPreco());
        assertFalse(resultado.getDisponivel());
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(produto1); // Verifica que o save foi chamado com o objeto existente atualizado
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar atualizar produto inexistente")
    void deveLancarRuntimeExceptionAoAtualizarProdutoInexistente() {
        // Arrange
        Produto produtoParaAtualizar = new Produto();
        produtoParaAtualizar.setNome("Inexistente");
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> produtoService.atualizar(99L, produtoParaAtualizar));

        assertEquals("Produto not found for update: 99", exception.getMessage());
        verify(produtoRepository, times(1)).findById(99L);
        verify(produtoRepository, never()).save(any(Produto.class)); // Não deve tentar salvar
    }

    // --- Testes para alterarDisponibilidade(Long id, boolean disponivel) ---

    @Test
    @DisplayName("Deve alterar a disponibilidade do produto para true")
    void deveAlterarDisponibilidadeParaTrue() {
        // Arrange
        produto1.setDisponivel(false); // Começa como indisponível
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto1); // Mock do save

        // Act
        produtoService.alterarDisponibilidade(1L, true);

        // Assert
        assertTrue(produto1.getDisponivel()); // Verifica se o objeto mockado foi atualizado
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(produto1);
    }

    @Test
    @DisplayName("Deve alterar a disponibilidade do produto para false")
    void deveAlterarDisponibilidadeParaFalse() {
        // Arrange
        produto1.setDisponivel(true); // Começa como disponível
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto1));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto1); // Mock do save

        // Act
        produtoService.alterarDisponibilidade(1L, false);

        // Assert
        assertFalse(produto1.getDisponivel()); // Verifica se o objeto mockado foi atualizado
        verify(produtoRepository, times(1)).findById(1L);
        verify(produtoRepository, times(1)).save(produto1);
    }

    @Test
    @DisplayName("Não deve fazer nada se o produto não for encontrado ao alterar disponibilidade")
    void naoDeveFazerNadaSeProdutoNaoEncontradoAoAlterarDisponibilidade() {
        // Arrange
        when(produtoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertDoesNotThrow(() -> produtoService.alterarDisponibilidade(99L, true)); // Não deve lançar exceção

        // Assert
        verify(produtoRepository, times(1)).findById(99L);
        verify(produtoRepository, never()).save(any(Produto.class)); // Não deve tentar salvar
    }
}
