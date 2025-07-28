package com.deliverytech.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.repository.RestauranteRepository;
import com.deliverytech.api.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class RestauranteServiceImplTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private RestauranteServiceImpl restauranteService;

    private Restaurante restaurante1;
    private Restaurante restaurante2;

    @BeforeEach
    void setUp() {
        restaurante1 = new Restaurante();
        restaurante1.setId(1L);
        restaurante1.setNome("Pizzaria Deliciosa");
        restaurante1.setTelefone("551112345678");
        restaurante1.setCategoria("Pizza");
        restaurante1.setTaxaEntrega(new BigDecimal("7.50"));
        restaurante1.setTempoEntregaMinutos(30);
        restaurante1.setAtivo(true);

        restaurante2 = new Restaurante();
        restaurante2.setId(2L);
        restaurante2.setNome("Sushi Master");
        restaurante2.setTelefone("551198765432");
        restaurante2.setCategoria("Japonesa");
        restaurante2.setTaxaEntrega(new BigDecimal("10.00"));
        restaurante2.setTempoEntregaMinutos(45);
        restaurante2.setAtivo(true);
    }

    // --- Testes para cadastrar(Restaurante restaurante) ---

    @Test
    @DisplayName("Deve cadastrar um novo restaurante com sucesso")
    void deveCadastrarRestauranteComSucesso() {
        // Arrange
        Restaurante novoRestaurante = new Restaurante();
        novoRestaurante.setNome("Novo Restaurante Teste");
        novoRestaurante.setTelefone("5511999999999");
        novoRestaurante.setCategoria("Brasileira");
        novoRestaurante.setTaxaEntrega(new BigDecimal("10.00"));
        novoRestaurante.setTempoEntregaMinutos(45);
        novoRestaurante.setAtivo(true);

        when(restauranteRepository.findByNome(anyString())).thenReturn(Optional.empty()); // Nome não existe
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(novoRestaurante);

        // Act
        Restaurante resultado = restauranteService.cadastrar(novoRestaurante);

        // Assert
        assertNotNull(resultado);
        assertEquals(novoRestaurante.getNome(), resultado.getNome());
        verify(restauranteRepository, times(1)).findByNome("Novo Restaurante Teste");
        verify(restauranteRepository, times(1)).save(novoRestaurante);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException (CONFLICT) ao tentar cadastrar restaurante com nome duplicado")
    void deveLancarConflictExceptionAoCadastrarRestauranteComNomeDuplicado() {
        // Arrange
        Restaurante restauranteDuplicado = new Restaurante();
        restauranteDuplicado.setNome("Pizzaria Deliciosa"); // Nome já existente
        restauranteDuplicado.setTelefone("5511999999999");
        restauranteDuplicado.setCategoria("Pizza");
        restauranteDuplicado.setTaxaEntrega(new BigDecimal("5.00"));
        restauranteDuplicado.setTempoEntregaMinutos(30);
        restauranteDuplicado.setAtivo(true);

        when(restauranteRepository.findByNome(anyString())).thenReturn(Optional.of(restaurante1)); // Nome já existe

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restauranteService.cadastrar(restauranteDuplicado));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Já existe um restaurante cadastrado com este nome: Pizzaria Deliciosa", exception.getReason());
        verify(restauranteRepository, times(1)).findByNome("Pizzaria Deliciosa");
        verify(restauranteRepository, never()).save(any(Restaurante.class)); // Não deve tentar salvar
    }

    // --- Testes para listarTodos() ---

    @Test
    @DisplayName("Deve listar todos os restaurantes")
    void deveListarTodosOsRestaurantes() {
        // Arrange
        List<Restaurante> todosOsRestaurantes = Arrays.asList(restaurante1, restaurante2);
        when(restauranteRepository.findAll()).thenReturn(todosOsRestaurantes);

        // Act
        List<Restaurante> resultado = restauranteService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(restaurante1));
        assertTrue(resultado.contains(restaurante2));
        verify(restauranteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver restaurantes cadastrados")
    void deveRetornarListaVaziaSeNaoHouverRestaurantesCadastrados() {
        // Arrange
        when(restauranteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Restaurante> resultado = restauranteService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(restauranteRepository, times(1)).findAll();
    }

    // --- Testes para buscarPorId(Long id) ---

    @Test
    @DisplayName("Deve buscar restaurante por ID existente")
    void deveBuscarRestaurantePorIdExistente() {
        // Arrange
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante1));

        // Act
        Optional<Restaurante> resultado = restauranteService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(restaurante1.getNome(), resultado.get().getNome());
        verify(restauranteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID de restaurante inexistente")
    void deveRetornarOptionalVazioParaIdInexistente() {
        // Arrange
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Restaurante> resultado = restauranteService.buscarPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(restauranteRepository, times(1)).findById(99L);
    }

    // --- Testes para buscarPorCategoria(String categoria) ---

    @Test
    @DisplayName("Deve buscar restaurantes por categoria existente")
    void deveBuscarRestaurantesPorCategoriaExistente() {
        // Arrange
        List<Restaurante> restaurantesDePizza = Arrays.asList(restaurante1);
        when(restauranteRepository.findByCategoria("Pizza")).thenReturn(restaurantesDePizza);

        // Act
        List<Restaurante> resultado = restauranteService.buscarPorCategoria("Pizza");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(restaurante1.getNome(), resultado.get(0).getNome());
        verify(restauranteRepository, times(1)).findByCategoria("Pizza");
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver restaurantes para a categoria")
    void deveRetornarListaVaziaParaCategoriaInexistente() {
        // Arrange
        when(restauranteRepository.findByCategoria(anyString())).thenReturn(Collections.emptyList());

        // Act
        List<Restaurante> resultado = restauranteService.buscarPorCategoria("Inexistente");

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(restauranteRepository, times(1)).findByCategoria("Inexistente");
    }

    // --- Testes para atualizar(Long id, Restaurante restauranteDetalhes) ---

    @Test
    @DisplayName("Deve atualizar um restaurante existente com sucesso")
    void deveAtualizarRestauranteExistenteComSucesso() {
        // Arrange
        Restaurante restauranteDetalhes = new Restaurante();
        restauranteDetalhes.setNome("Pizzaria Atualizada");
        restauranteDetalhes.setTelefone("5511999998888");
        restauranteDetalhes.setTaxaEntrega(new BigDecimal("8.00"));
        restauranteDetalhes.setAtivo(false);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante1));
        when(restauranteRepository.findByNome(anyString())).thenReturn(Optional.empty()); // Sem conflito de nome
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante1); // Retorna o mesmo objeto mockado

        // Act
        Restaurante resultado = restauranteService.atualizar(1L, restauranteDetalhes);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pizzaria Atualizada", resultado.getNome());
        assertEquals("5511999998888", resultado.getTelefone());
        assertEquals(new BigDecimal("8.00"), resultado.getTaxaEntrega());
        assertFalse(resultado.getAtivo());
        verify(restauranteRepository, times(1)).findById(1L);
        verify(restauranteRepository, times(1)).findByNome("Pizzaria Atualizada");
        verify(restauranteRepository, times(1)).save(restaurante1);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException (NOT_FOUND) ao tentar atualizar restaurante inexistente")
    void deveLancarNotFoundExceptionAoAtualizarRestauranteInexistente() {
        // Arrange
        Restaurante restauranteDetalhes = new Restaurante();
        restauranteDetalhes.setNome("Inexistente");
        when(restauranteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restauranteService.atualizar(99L, restauranteDetalhes));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Restaurante não encontrado com ID: 99", exception.getReason());
        verify(restauranteRepository, times(1)).findById(99L);
        verify(restauranteRepository, never()).findByNome(anyString());
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException (CONFLICT) ao tentar atualizar restaurante com nome para um nome já existente")
    void deveLancarConflictExceptionAoAtualizarRestauranteComNomeDuplicado() throws Exception {
        // Arrange
        Restaurante restauranteParaAtualizar = new Restaurante();
        restauranteParaAtualizar.setNome("Sushi Master"); // Tentando mudar o nome para um que já existe
        restauranteParaAtualizar.setTelefone("551112345678");
        restauranteParaAtualizar.setCategoria("Pizza");
        restauranteParaAtualizar.setTaxaEntrega(new BigDecimal("7.50"));
        restauranteParaAtualizar.setTempoEntregaMinutos(30);
        restauranteParaAtualizar.setAtivo(true);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante1)); // Encontra o restaurante a ser atualizado
        when(restauranteRepository.findByNome("Sushi Master")).thenReturn(Optional.of(restaurante2)); // Encontra outro restaurante com o nome desejado

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restauranteService.atualizar(1L, restauranteParaAtualizar));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Já existe outro restaurante cadastrado com este nome: Sushi Master", exception.getReason());
        verify(restauranteRepository, times(1)).findById(1L);
        verify(restauranteRepository, times(1)).findByNome("Sushi Master");
        verify(restauranteRepository, never()).save(any(Restaurante.class));
    }

    @Test
    @DisplayName("Não deve lançar CONFLICT se o nome atualizado for o mesmo do restaurante")
    void naoDeveLancarConflictSeNomeForOProprioRestaurante() throws Exception {
        // Arrange
        Restaurante restauranteDetalhes = new Restaurante();
        restauranteDetalhes.setNome("Pizzaria Deliciosa"); // Mesmo nome do restaurante1
        restauranteDetalhes.setTelefone("5511999998888");
        restauranteDetalhes.setTaxaEntrega(new BigDecimal("8.00"));
        restauranteDetalhes.setAtivo(false);

        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante1));
        when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante1);

        // Act & Assert
        assertDoesNotThrow(() -> restauranteService.atualizar(1L, restauranteDetalhes));

        verify(restauranteRepository, times(1)).findById(1L);
        verify(restauranteRepository, never()).findByNome(anyString()); // Não deve chamar findByNome se o nome não mudou
        verify(restauranteRepository, times(1)).save(restaurante1);
    }

    // --- Testes para findByNome(String nome) ---

    @Test
    @DisplayName("Deve retornar true se o nome do restaurante existir")
    void deveRetornarTrueSeNomeRestauranteExistir() {
        // Arrange
        when(restauranteRepository.findByNome("Pizzaria Deliciosa")).thenReturn(Optional.of(restaurante1));

        // Act
        boolean existe = restauranteService.findByNome("Pizzaria Deliciosa");

        // Assert
        assertTrue(existe);
        verify(restauranteRepository, times(1)).findByNome("Pizzaria Deliciosa");
    }

    @Test
    @DisplayName("Deve retornar false se o nome do restaurante não existir")
    void deveRetornarFalseSeNomeRestauranteNaoExistir() {
        // Arrange
        when(restauranteRepository.findByNome(anyString())).thenReturn(Optional.empty());

        // Act
        boolean existe = restauranteService.findByNome("Restaurante Inexistente");

        // Assert
        assertFalse(existe);
        verify(restauranteRepository, times(1)).findByNome("Restaurante Inexistente");
    }

    // --- Testes para deletar(Long id) ---

    @Test
    @DisplayName("Deve deletar um restaurante existente com sucesso")
    void deveDeletarRestauranteExistente() {
        // Arrange
        when(restauranteRepository.existsById(1L)).thenReturn(true);
        when(produtoRepository.findByRestauranteId(1L)).thenReturn(Collections.emptyList());
        doNothing().when(restauranteRepository).deleteById(1L);

        // Act & Assert
        assertDoesNotThrow(() -> restauranteService.deletar(1L));

        // Assert
        verify(restauranteRepository, times(1)).existsById(1L);
        verify(produtoRepository, times(1)).findByRestauranteId(1L);
        verify(restauranteRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResponseStatusException (NOT_FOUND) ao tentar deletar restaurante inexistente")
    void deveLancarNotFoundExceptionAoDeletarRestauranteInexistente() {
        // Arrange
        when(restauranteRepository.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> restauranteService.deletar(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Restaurante não encontrado com ID: 99", exception.getReason());
        verify(restauranteRepository, times(1)).existsById(99L);
        verify(restauranteRepository, never()).deleteById(anyLong());
    }
}
