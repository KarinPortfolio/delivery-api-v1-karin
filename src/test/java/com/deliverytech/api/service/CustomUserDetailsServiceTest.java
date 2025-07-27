package com.deliverytech.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private Usuario testUser;

    @BeforeEach
    void setUp() {
        testUser = new Usuario();
        testUser.setId(1L);
        testUser.setNome("Test User");
        testUser.setEmail("test@example.com");
        testUser.setSenha("encodedPassword"); // Senha já codificada
        testUser.setRole(Role.CLIENTE); // Exemplo de role
        testUser.setAtivo(true);
    }

    @Test
    @DisplayName("Deve carregar UserDetails quando o usuário é encontrado pelo email")
    void loadUserByUsername_UserFound() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals(testUser.getEmail(), userDetails.getUsername());
        assertEquals(testUser.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().containsAll(testUser.getAuthorities()));
        assertEquals(testUser.getAuthorities().size(), userDetails.getAuthorities().size());
        verify(usuarioRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o usuário não é encontrado")
    void loadUserByUsername_UserNotFound() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("nonexistent@example.com"));

        assertEquals("Usuário não encontrado com email: nonexistent@example.com", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
