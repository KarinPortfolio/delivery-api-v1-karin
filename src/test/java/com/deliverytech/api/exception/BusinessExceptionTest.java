package com.deliverytech.api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void deveTestarConstrutorComMensagem() {
        // Arrange
        String message = "Erro de negócio";

        // Act
        BusinessException exception = new BusinessException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getErrorCode());
    }

    @Test
    void deveTestarConstrutorComMensagemECodigo() {
        // Arrange
        String message = "Erro de negócio";
        String errorCode = "BUSINESS_ERROR";

        // Act
        BusinessException exception = new BusinessException(message, errorCode);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
    }

    @Test
    void deveTestarConstrutorComMensagemECause() {
        // Arrange
        String message = "Erro de negócio";
        Throwable cause = new RuntimeException("Causa original");

        // Act
        BusinessException exception = new BusinessException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void deveTestarConstrutorCompleto() {
        // Arrange
        String message = "Erro de negócio";
        String errorCode = "BUSINESS_ERROR";
        Throwable cause = new RuntimeException("Causa original");

        // Act
        BusinessException exception = new BusinessException(message, errorCode, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void deveTestarSetterDoErrorCode() {
        // Arrange
        BusinessException exception = new BusinessException("Test");
        String errorCode = "NEW_CODE";

        // Act
        exception.setErrorCode(errorCode);

        // Assert
        assertEquals(errorCode, exception.getErrorCode());
    }
}
