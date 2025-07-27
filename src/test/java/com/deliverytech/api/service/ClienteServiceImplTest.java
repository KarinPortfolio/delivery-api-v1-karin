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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente clienteAtivo;
    private Cliente clienteInativo;

    @BeforeEach
    void setUp() {
        clienteAtivo = Cliente.builder()
                .id(1L)
                .nome("Cliente Ativo")
                .email("ativo@example.com")
                .ativo(true)
                .build();

        clienteInativo = Cliente.builder()
                .id(2L)
                .nome("Cliente Inativo")
                .email("inativo@example.com")
                .ativo(false)
                .build();
    }

    // --- Testes para cadastrar(Cliente cliente) ---

    @Test
    @DisplayName("Deve cadastrar um novo cliente com sucesso")
    void deveCadastrarClienteComSucesso() {
        // Arrange
        Cliente novoCliente = Cliente.builder()
                .nome("Novo Cliente")
                .email("novo@example.com")
                .ativo(true)
                .build();
        
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(novoCliente);

        // Act
        Cliente resultado = clienteService.cadastrar(novoCliente);

        // Assert
        assertNotNull(resultado);
        assertEquals(novoCliente.getEmail(), resultado.getEmail());
        verify(clienteRepository, times(1)).existsByEmail("novo@example.com");
        verify(clienteRepository, times(1)).save(novoCliente);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar cadastrar cliente com email duplicado")
    void deveLancarExcecaoAoCadastrarComEmailDuplicado() {
        // Arrange
        Cliente clienteDuplicado = Cliente.builder()
                .nome("Cliente Duplicado")
                .email("ativo@example.com")
                .ativo(true)
                .build();
        
        when(clienteRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.cadastrar(clienteDuplicado));

        assertEquals("Já existe um cliente cadastrado com este email: ativo@example.com", exception.getMessage());
        verify(clienteRepository, times(1)).existsByEmail("ativo@example.com");
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // --- Testes para buscarPorId(Long id) ---

    @Test
    @DisplayName("Deve buscar cliente por ID existente")
    void deveBuscarClientePorIdExistente() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteAtivo));

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(clienteAtivo.getNome(), resultado.get().getNome());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio para ID de cliente inexistente")
    void deveRetornarOptionalVazioParaIdInexistente() {
        // Arrange
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Cliente> resultado = clienteService.buscarPorId(99L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(99L);
    }

    // --- Testes para listarAtivos() ---

    @Test
    @DisplayName("Deve listar todos os clientes ativos")
    void deveListarTodosOsClientesAtivos() {
        // Arrange
        List<Cliente> clientesAtivos = Arrays.asList(clienteAtivo);
        when(clienteRepository.findByAtivoTrue()).thenReturn(clientesAtivos);

        // Act
        List<Cliente> resultado = clienteService.listarAtivos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(clienteAtivo));
        verify(clienteRepository, times(1)).findByAtivoTrue();
    }

    @Test
    @DisplayName("Deve retornar lista vazia se não houver clientes ativos")
    void deveRetornarListaVaziaSeNaoHouverClientesAtivos() {
        // Arrange
        when(clienteRepository.findByAtivoTrue()).thenReturn(Collections.emptyList());

        // Act
        List<Cliente> resultado = clienteService.listarAtivos();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(clienteRepository, times(1)).findByAtivoTrue();
    }

    // --- Testes para atualizar(Long id, Cliente clienteAtualizado) ---

    @Test
    @DisplayName("Deve atualizar um cliente existente com sucesso")
    void deveAtualizarClienteExistenteComSucesso() {
        // Arrange
        Cliente clienteParaAtualizar = Cliente.builder()
                .nome("Cliente Atualizado")
                .email("atualizado@example.com")
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteAtivo));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtivo); // Mock do save

        // Act
        Cliente resultado = clienteService.atualizar(1L, clienteParaAtualizar);

        // Assert
        assertNotNull(resultado);
        assertEquals("Cliente Atualizado", resultado.getNome());
        assertEquals("atualizado@example.com", resultado.getEmail());
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(clienteAtivo); // Verifica que o save foi chamado com o objeto existente atualizado
    }

    @Test
    @DisplayName("Deve lançar RuntimeException ao tentar atualizar cliente inexistente")
    void deveLancarRuntimeExceptionAoAtualizarClienteInexistente() {
        // Arrange
        Cliente clienteParaAtualizar = Cliente.builder().nome("Inexistente").build();
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.atualizar(99L, clienteParaAtualizar));

        assertEquals("Cliente não encontrado para atualização: 99", exception.getMessage());
        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    // --- Testes para ativarDesativar(Long id) ---

    @Test
    @DisplayName("Deve ativar um cliente inativo")
    void deveAtivarClienteInativo() {
        // Arrange
        when(clienteRepository.findById(2L)).thenReturn(Optional.of(clienteInativo));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteInativo); // Mock do save

        // Act
        clienteService.ativarDesativar(2L);

        // Assert
        assertTrue(clienteInativo.getAtivo()); // Verifica se o objeto mockado foi atualizado
        verify(clienteRepository, times(1)).findById(2L);
        verify(clienteRepository, times(1)).save(clienteInativo);
    }

    @Test
    @DisplayName("Deve desativar um cliente ativo")
    void deveDesativarClienteAtivo() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteAtivo));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteAtivo); // Mock do save

        // Act
        clienteService.ativarDesativar(1L);

        // Assert
        assertFalse(clienteAtivo.getAtivo()); // Verifica se o objeto mockado foi atualizado
        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(clienteAtivo);
    }

    @Test
    @DisplayName("Não deve fazer nada se o cliente não for encontrado ao ativar/desativar")
    void naoDeveFazerNadaSeClienteNaoEncontradoAoAtivarDesativar() {
        // Arrange
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertDoesNotThrow(() -> clienteService.ativarDesativar(99L)); // Não deve lançar exceção

        // Assert
        verify(clienteRepository, times(1)).findById(99L);
        verify(clienteRepository, never()).save(any(Cliente.class)); // Não deve tentar salvar
    }
}
