package com.deliverytech.api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void deveTestarConstrutorVazio() {
        // Act
        ErrorResponse response = new ErrorResponse();

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveTestarConstrutorCompleto() {
        // Arrange
        int status = 404;
        String error = "Not Found";
        String message = "Recurso não encontrado";
        String path = "/api/test";

        // Act
        ErrorResponse response = new ErrorResponse(status, error, message, path);

        // Assert
        assertEquals(status, response.getStatus());
        assertEquals(error, response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void deveTestarGettersESetters() {
        // Arrange
        ErrorResponse response = new ErrorResponse();
        String errorCode = "TEST_ERROR";
        String message = "Mensagem de teste";

        // Act
        response.setErrorCode(errorCode);
        response.setMessage(message);

        // Assert
        assertEquals(errorCode, response.getErrorCode());
        assertEquals(message, response.getMessage());
    }
}
