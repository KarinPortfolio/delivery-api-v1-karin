package com.deliverytech.api.dto.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void devecriarApiResponseComStatus() {
        // Given
        int status = 200;
        String message = "Sucesso";
        
        // When
        ApiResponse<String> response = new ApiResponse<>(status, message);
        
        // Then
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveCriarApiResponseComStatusEData() {
        // Given
        int status = 201;
        String message = "Criado com sucesso";
        String data = "Dados de teste";
        
        // When
        ApiResponse<String> response = new ApiResponse<>(status, message, data);
        
        // Then
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveCriarResponseDeSucessoComData() {
        // Given
        String message = "Operação realizada com sucesso";
        String data = "Dados de retorno";
        
        // When
        ApiResponse<String> response = ApiResponse.success(message, data);
        
        // Then
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveCriarResponseDeSucessoSemData() {
        // Given
        String message = "Operação realizada com sucesso";
        
        // When
        ApiResponse<String> response = ApiResponse.success(message);
        
        // Then
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveCriarResponseDeErro() {
        // Given
        int status = 400;
        String message = "Erro na requisição";
        
        // When
        ApiResponse<String> response = ApiResponse.error(status, message);
        
        // Then
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveDefinirEObterStatus() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();
        int status = 404;
        
        // When
        response.setStatus(status);
        
        // Then
        assertEquals(status, response.getStatus());
    }

    @Test
    void deveDefinirEObterMessage() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();
        String message = "Recurso não encontrado";
        
        // When
        response.setMessage(message);
        
        // Then
        assertEquals(message, response.getMessage());
    }

    @Test
    void deveDefinirEObterData() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();
        String data = "Dados de teste";
        
        // When
        response.setData(data);
        
        // Then
        assertEquals(data, response.getData());
    }

    @Test
    void deveDefinirEObterTimestamp() {
        // Given
        ApiResponse<String> response = new ApiResponse<>();
        String timestamp = "2025-01-27T10:30:00Z";
        
        // When
        response.setTimestamp(timestamp);
        
        // Then
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    void deveCriarApiResponseVaziaComTimestamp() {
        // When
        ApiResponse<String> response = new ApiResponse<>();
        
        // Then
        assertNotNull(response.getTimestamp());
        assertEquals(0, response.getStatus());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }
}
