
package com.deliverytech.api.exception;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
     @SuppressWarnings("unused")
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
    }

    @Test
    void deveManipularMethodArgumentNotValidException() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError fieldError1 = new FieldError("object", "nome", "Nome é obrigatório");
        FieldError fieldError2 = new FieldError("object", "email", "Email inválido");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Erro de validação nos dados enviados", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertNotNull(body.getPath());
    }

    @Test
    void deveManipularEntityNotFoundException() {
        // Given
        EntityNotFoundException ex = new EntityNotFoundException("Usuario", 1L);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFoundException(ex, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertTrue(body.getMessage().contains("Usuario"));
        assertTrue(body.getMessage().contains("1"));
        assertNotNull(body.getTimestamp());
        assertNotNull(body.getPath());
    }

    @Test
    void deveManipularConflictException() {
        // Given
        ConflictException ex = new ConflictException("Email já existe");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleConflictException(ex, webRequest);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.get("status"));
        assertEquals("Email já existe", body.get("message"));
        assertNotNull(body.get("timestamp"));
        assertNotNull(body.get("path"));
    }

    @Test
    void deveManipularExcecaoGenerica() {
        // Given
        RuntimeException ex = new RuntimeException("Erro interno");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/usuarios");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("Erro interno do servidor", body.getError());
        assertEquals("Ocorreu um erro inesperado. Tente novamente mais tarde.", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertNotNull(body.getPath());
    }

    @Test
    void deveManipularExcecaoGenericaNaoDeLogin() {
        // Given
        RuntimeException ex = new RuntimeException("Erro interno");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/usuarios");

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(ex, webRequest);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("Ocorreu um erro inesperado. Tente novamente mais tarde.", body.getMessage());
        assertNull(body.getDetails());
        assertNotNull(body.getTimestamp());
        assertNotNull(body.getPath());
    }

    @Test
    void deveManipularValidationExceptionSemErros() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Erro de validação nos dados enviados", body.getMessage());
    }

    @Test
    void deveManipularEntityNotFoundExceptionComParametrosNulos() {
        // Given
        EntityNotFoundException ex = new EntityNotFoundException(null, null);

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFoundException(ex, webRequest);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertNotNull(body.getMessage());
        assertNotNull(body.getTimestamp());
        assertNotNull(body.getPath());
    }

    @Test
    void deveManipularConflictExceptionComMensagemNula() {
        // Given
        ConflictException ex = new ConflictException(null);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleConflictException(ex, webRequest);

        // Then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.get("status"));
        assertNotNull(body.get("timestamp"));
        assertNotNull(body.get("path"));
    }

    @Test
    void deveManipularDiferentesTiposDeValidationErrors() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        
        FieldError campoObrigatorio = new FieldError("usuario", "nome", "Campo obrigatório");
        FieldError emailInvalido = new FieldError("usuario", "email", "Formato de email inválido");  
        FieldError senhaFraca = new FieldError("usuario", "senha", "Senha deve ter pelo menos 6 caracteres");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(campoObrigatorio, emailInvalido, senhaFraca));

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Erro de validação nos dados enviados", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertNotNull(body.getPath());
    }

    @Test
    void deveManipularEntityNotFoundComDiferentesTipos() {
        // Given
        String[] entidades = {"Usuario", "Produto", "Pedido", "Restaurante"};
        Long[] ids = {1L, 2L, 100L, 999L};

        for (int i = 0; i < entidades.length; i++) {
            EntityNotFoundException ex = new EntityNotFoundException(entidades[i], ids[i]);

            // When
            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFoundException(ex, webRequest);

            // Then
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            ErrorResponse body = response.getBody();
            assertNotNull(body);
            assertNotNull(body);
            assertTrue(body.getMessage().contains(entidades[i]));
            assertTrue(body.getMessage().contains(ids[i].toString()));
        }
    }
}
