package com.deliverytech.api.dto.request;

import com.deliverytech.api.model.Role;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void deveCriarRegisterRequestVazio() {
        // When
        RegisterRequest request = new RegisterRequest();
        
        // Then
        assertNull(request.getNome());
        assertNull(request.getEmail());
        assertNull(request.getSenha());
        assertNull(request.getRole());
        assertNull(request.getRestauranteId());
    }

    @Test
    void deveDefinirEObterTodosOsCampos() {
        // Given
        RegisterRequest request = new RegisterRequest();
        String nome = "João Silva";
        String email = "joao@teste.com";
        String senha = "123456";
        Role role = Role.CLIENTE;
        Long restauranteId = 1L;
        
        // When
        request.setNome(nome);
        request.setEmail(email);
        request.setSenha(senha);
        request.setRole(role);
        request.setRestauranteId(restauranteId);
        
        // Then
        assertEquals(nome, request.getNome());
        assertEquals(email, request.getEmail());
        assertEquals(senha, request.getSenha());
        assertEquals(role, request.getRole());
        assertEquals(restauranteId, request.getRestauranteId());
    }

    @Test
    void deveValidarRegisterRequestValido() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("Maria Santos");
        request.setEmail("maria@teste.com");
        request.setSenha("senha123");
        request.setRole(Role.CLIENTE);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertTrue(violations.isEmpty());
    }

    @Test
    void deveRejeitarNomeVazio() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("");
        request.setEmail("teste@teste.com");
        request.setSenha("123456");
        request.setRole(Role.CLIENTE);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Nome é obrigatório")));
    }

    @Test
    void deveRejeitarNomeNulo() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome(null);
        request.setEmail("teste@teste.com");
        request.setSenha("123456");
        request.setRole(Role.CLIENTE);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Nome é obrigatório")));
    }

    @Test
    void deveRejeitarEmailVazio() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("João Silva");
        request.setEmail("");
        request.setSenha("123456");
        request.setRole(Role.CLIENTE);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email é obrigatório")));
    }

    @Test
    void deveRejeitarEmailInvalido() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("João Silva");
        request.setEmail("email-invalido");
        request.setSenha("123456");
        request.setRole(Role.CLIENTE);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email deve ser um formato válido")));
    }

    @Test
    void deveRejeitarSenhaVazia() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("João Silva");
        request.setEmail("joao@teste.com");
        request.setSenha("");
        request.setRole(Role.CLIENTE);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Senha é obrigatória")));
    }

    @Test
    void deveRejeitarRoleNula() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("João Silva");
        request.setEmail("joao@teste.com");
        request.setSenha("123456");
        request.setRole(null);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Role é obrigatória")));
    }

    @Test
    void deveAceitarTodasAsRoles() {
        // Given
        Role[] roles = {Role.CLIENTE, Role.ADMIN, Role.ENTREGADOR};
        
        for (Role role : roles) {
            RegisterRequest request = new RegisterRequest();
            request.setNome("Teste Usuario");
            request.setEmail("teste@teste.com");
            request.setSenha("123456");
            request.setRole(role);
            
            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
            
            // Then
            assertTrue(violations.isEmpty(), "Deve aceitar role: " + role);
        }
    }

    @Test
    void devePermitirRestauranteIdNulo() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("João Silva");
        request.setEmail("joao@teste.com");
        request.setSenha("123456");
        request.setRole(Role.CLIENTE);
        request.setRestauranteId(null);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertTrue(violations.isEmpty());
        assertNull(request.getRestauranteId());
    }

    @Test
    void devePermitirRestauranteIdValido() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setNome("João Silva");
        request.setEmail("joao@teste.com");
        request.setSenha("123456");
        request.setRole(Role.ADMIN);
        request.setRestauranteId(5L);
        
        // When
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        
        // Then
        assertTrue(violations.isEmpty());
        assertEquals(5L, request.getRestauranteId());
    }

    @Test
    void deveValidarEmailsValidos() {
        // Given
        String[] emailsValidos = {
            "usuario@exemplo.com",
            "teste.email@dominio.com.br",
            "admin@site.org",
            "user123@test.io"
        };
        
        for (String email : emailsValidos) {
            RegisterRequest request = new RegisterRequest();
            request.setNome("Teste");
            request.setEmail(email);
            request.setSenha("123456");
            request.setRole(Role.CLIENTE);
            
            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
            
            // Then
            assertTrue(violations.isEmpty(), "Deve aceitar email válido: " + email);
        }
    }
}
